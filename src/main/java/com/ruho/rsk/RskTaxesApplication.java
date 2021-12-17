package com.ruho.rsk;

import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.http.TransactionsFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class RskTaxesApplication implements CommandLineRunner {
	@Autowired
	private TransactionsFetcherService transactionsFetcherService;

	public static void main(String[] args) {
		new SpringApplicationBuilder(RskTaxesApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
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
		do {
			if (apiKey.equals("--classpath")) {
				rskDto = fromClasspath(walletAddress, pageNumber);
			} else {
				rskDto = this.transactionsFetcherService.fetchTransactions(walletAddress, apiKey, pageNumber);
			}
			new TransactionsParser().parse(rskDto).forEach(report -> {
				System.out.println(report);
				System.out.println("------------------");
			});
			pageNumber++;
		} while(rskDto.getData().getPagination().getHasMore());
		System.out.println("completed!");
		System.exit(0);
	}

	private RskDto fromClasspath(String walletAddr, int pageNumber) throws IOException {
		String filePath = Objects.requireNonNull(
				TransactionsParser.class.getClassLoader().getResource(String.format("transactions/%s-%s.json", walletAddr, pageNumber))
		).getFile();
		return this.transactionsFetcherService.fetchTransactions(filePath);
	}
}
