package com.ruho.rsk;

import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.http.TransactionsFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class RskTaxesApplication implements CommandLineRunner {
	@Autowired
	private TransactionsFetcherService transactionsFetcherService;

	public static void main(String[] args) {
		SpringApplication.run(RskTaxesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("starting");
		RskDto rskDto;
		if(args.length > 0 && args[0].equals("classpath")) {
			rskDto = fromClasspath();
		} else {
			rskDto = this.transactionsFetcherService.fetchTransactions();
		}
		new TransactionsParser().parse(rskDto).forEach(report -> {
			System.out.println(report);
			System.out.println("------------------");
		});
		System.out.println("completed!");
		System.exit(0);
	}

	private RskDto fromClasspath() throws IOException {
		String filePath = Objects.requireNonNull(TransactionsParser.class.getClassLoader().getResource("rsk.json")).getFile();
		return this.transactionsFetcherService.fetchTransactions(filePath);
	}
}
