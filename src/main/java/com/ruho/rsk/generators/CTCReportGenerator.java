package com.ruho.rsk.generators;

import com.ruho.rsk.filters.reports.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CTCReportGenerator {
    private final static boolean VESTING_WITHDRAW_MODE_WITH_LP_TOKEN = true;


    private List<String[]> generate(AnyReport report) {
        if(report instanceof SpotSwapReport) {
            return spotSwap((SpotSwapReport) report);
        } else if( report instanceof VestingWithdrawReport) {
            return withdrawVesting((VestingWithdrawReport) report);
        } else if(report instanceof AddLiquidityReport) {
            if(VESTING_WITHDRAW_MODE_WITH_LP_TOKEN) {
                return new CTCLiquidityWithLpTokenGen().addLiquidity((AddLiquidityReport) report);
            }
        } else if(report instanceof RemoveLiquidityReport) {
            if(VESTING_WITHDRAW_MODE_WITH_LP_TOKEN) {
                return new CTCLiquidityWithLpTokenGen().removeLiquidity((RemoveLiquidityReport) report);
            }
        } else if(report instanceof DepositReport) {
            return deposit((DepositReport) report);
        }
        return new ArrayList<>();
    }

    private List<String[]> spotSwap(SpotSwapReport report) {
        SpotSwapReport swapReport = report;
        return toList(
            CTCPrinter.printToList(
                swapReport.getTime(),
                "buy",
                swapReport.getTargetSymbol(),
                swapReport.getTargetAmount(),
                swapReport.getSourceSymbol(),
                swapReport.getSourceAmount(),
                swapReport.getFees(),
                "swap"
            ).toArray(new String[0])
        );
    }

    private List<String[]> deposit(DepositReport report) {
        DepositReport depositReport = report;
        return toList(
                CTCPrinter.printToList(
                        depositReport.getTime(),
                        "transfer-in",
                        depositReport.getSymbol(),
                        depositReport.getAmount(),
                        "",
                        null,
                        null,
                        "deposit"
                ).toArray(new String[0])
        );
    }

    private List<String[]> withdrawVesting(VestingWithdrawReport report) {
        VestingWithdrawReport vestingWithdrawReport = report;
        return toList(
                CTCPrinter.printToList(
                        vestingWithdrawReport.getTime(),
                        "staking",
                        vestingWithdrawReport.getSymbol(),
                        vestingWithdrawReport.getAmount(),
                        "",
                        null,
                        vestingWithdrawReport.getFees(),
                        "VestingWithdrawReport"
                ).toArray(new String[0])
        );
    }

    private List<String[]> toList(String[] entry) {
        List<String[]> objects = new ArrayList<>();
        objects.add(entry);
        return objects;
    }

    public void generatorReport(List<AnyReport> reports) throws IOException {
        String[] HEADERS = new String[] {
                "Timestamp (UTC)", "Type", "Base Currency", "Base Amount", "Quote Currency (Optional)",
                "Quote Amount (Optional)", "Fee Currency (Optional)", "Fee Amount (Optional)",
                "From (Optional)", "To (Optional)", "ID (Optional)", "Description (Optional)"
        };
        FileWriter out = new FileWriter("ctc_report.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            for (AnyReport report : reports) {
                for (String[] entry : generate(report)) {
                    printer.printRecord(entry);
                }
            }
        }
    }
}
