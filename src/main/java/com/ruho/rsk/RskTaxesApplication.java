package com.ruho.rsk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.DepositReport;
import com.ruho.rsk.generators.CTCReportGenerator;
import com.ruho.rsk.http.TransactionsFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class RskTaxesApplication implements CommandLineRunner {

	@Autowired
	private TransactionsFetcherService transactionsFetcherService;

	@Autowired
	private TransactionsParser transactionsParser;

	public static void main(String[] args) {
		new SpringApplicationBuilder(RskTaxesApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	void printReport(String walletAddress, List<AnyReport> entries, RskInternalTransaction[] internalTransactions){
		CTCReportGenerator reportGenerator = new CTCReportGenerator();
		try {
			reportGenerator.generatorReport(combineReports(walletAddress, entries, internalTransactions));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	private List<AnyReport> combineReports(String walletAddress, List<AnyReport> entries, RskInternalTransaction[] internalTransactions) {
		Set<String> existingTransactions = entries.stream()
				.map(AnyReport::getTransactionHash)
				.map(String::toLowerCase)
				.collect(Collectors.toSet());
		List<AnyReport> allReports = Arrays.stream(internalTransactions)
				.filter(rskInternalTransaction -> rskInternalTransaction.getTo().equalsIgnoreCase(walletAddress))
				.filter(rskInternalTransaction -> !existingTransactions.contains(rskInternalTransaction.getTransaction().toLowerCase()))
				.filter(rskInternalTransaction -> rskInternalTransaction.getStatus().equalsIgnoreCase("SUCCESSFUL"))
				.map(this::mapToReport)
				.collect(Collectors.toList());
		allReports.addAll(entries);
		return allReports;
	}

	private AnyReport mapToReport(RskInternalTransaction rskInternalTransaction) {
		Instant instant = Instant.ofEpochSecond(rskInternalTransaction.getTimestamp());
		return new DepositReport()
				.setTransactionHash(rskInternalTransaction.getTransaction())
				.setTime(LocalDateTime.ofInstant(instant, ZoneOffset.UTC))
				.setSymbol(rskInternalTransaction.getValueSymbol())
				.setAmount(rskInternalTransaction.getValueAmount());
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length != 2) {
			System.out.println("please run it with \"<wallet_addr> --classpath\" or with \"<wallet_addr> <apiKey>\"");
			System.exit(1);
		}
		System.out.println("starting");
		RskDto rskDto;
		int pageNumber = 0;
		String walletAddress = args[0];
		String apiKey = args[1];
		List<AnyReport> allEntries = new ArrayList<>();
		do {
			if (apiKey.equals("--classpath")) {
				rskDto = fromClasspath(walletAddress, pageNumber);
			} else {
				rskDto = this.transactionsFetcherService.fetchTransactions(walletAddress, apiKey, pageNumber);
			}
			List<AnyReport> entries = transactionsParser.parse(rskDto);
			entries.forEach(report -> {
				System.out.println(report);
				System.out.println("------------------");
			});
			allEntries.addAll(entries);
			pageNumber++;
		} while(rskDto.getData().getPagination().getHasMore());
		RskInternalTransaction[] internalTransactions = internalTransactions(walletAddress);
		printReport(walletAddress, allEntries, internalTransactions);
		System.out.println("completed!");
		System.exit(0);
	}

	private RskDto fromClasspath(String walletAddr, int pageNumber) throws IOException {
		String filePath = Objects.requireNonNull(
				TransactionsParser.class.getClassLoader().getResource(String.format("transactions/%s-%s.json", walletAddr, pageNumber))
		).getFile();
		return this.transactionsFetcherService.fetchTransactions(filePath);
	}

	public RskInternalTransaction[] internalTransactions(String walletAddr) throws IOException {
		String filePathInternal = Objects.requireNonNull(
				TransactionsParser.class.getClassLoader().getResource(String.format("transactions/%s-internal.json", walletAddr))
		).getFile();
		ObjectMapper mapper = new ObjectMapper();
		try (Reader readerInternal = new FileReader(filePathInternal)) {
			return mapper.readValue(readerInternal, RskInternalTransaction[].class);
		}
	}
}
