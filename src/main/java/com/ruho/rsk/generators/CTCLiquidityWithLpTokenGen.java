package com.ruho.rsk.generators;

import com.ruho.rsk.filters.reports.AddLiquidityReport;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CTCLiquidityWithLpTokenGen {

    public List<String[]> addLiquidity(AddLiquidityReport report) {
        AddLiquidityReport addLiquidityReport = report;
        List<String[]> parts = new ArrayList<>();
        String description = String.format("add liquidity: %s %s and %s %s",
                                           addLiquidityReport.getBaseAmount(),
                                           addLiquidityReport.getBaseSymbol(),
                                           addLiquidityReport.getQuotedAmount(),
                                           addLiquidityReport.getQuotedSymbol());
        parts.add(sellEntry(addLiquidityReport, addLiquidityReport.getBaseSymbol(), addLiquidityReport.getBaseAmount()));
        parts.add(sellEntry(addLiquidityReport, addLiquidityReport.getQuotedSymbol(), addLiquidityReport.getQuotedAmount()));
        parts.add(
                CTCPrinter.printToList(
                        addLiquidityReport.getTime(),
                        "buy",
                        addLiquidityReport.getPoolTokenSymbol(),
                        addLiquidityReport.getPoolTokenAmount(),
                        "",
                        null,
                        addLiquidityReport.getFees(),
                        description
                ).toArray(new String[0])
        );
        return parts;
    }

    public List<String[]> removeLiquidity(RemoveLiquidityReport report) {
        RemoveLiquidityReport removeLiquidityReport = report;
        List<String[]> parts = new ArrayList<>();
        String description = String.format("removed liquidity: %s %s and %s %s",
                                           removeLiquidityReport.getBaseAmount(),
                                           removeLiquidityReport.getBaseSymbol(),
                                           removeLiquidityReport.getQuotedAmount(),
                                           removeLiquidityReport.getQuotedSymbol());
        parts.add(buyEntry(removeLiquidityReport, removeLiquidityReport.getBaseSymbol(), removeLiquidityReport.getBaseAmount()));
        parts.add(buyEntry(removeLiquidityReport, removeLiquidityReport.getQuotedSymbol(), removeLiquidityReport.getQuotedAmount()));
        parts.add(
                CTCPrinter.printToList(
                        removeLiquidityReport.getTime(),
                        "sell",
                        removeLiquidityReport.getPoolTokenSymbol(),
                        removeLiquidityReport.getPoolTokenAmount(),
                        "",
                        null,
                        removeLiquidityReport.getFees(),
                        description
                ).toArray(new String[0])
        );
        return parts;
    }


    private String[] sellEntry(AddLiquidityReport report, String symbol, BigDecimal amount) {
        return CTCPrinter.printToList(
                report.getTime(),
                "sell",
                symbol,
                amount,
                "",
                null,
                null,
                "AddLiquidityReport - Sell"
        ).toArray(new String[0]);
    }

    private String[] buyEntry(RemoveLiquidityReport report, String symbol, BigDecimal amount) {
        return CTCPrinter.printToList(
                report.getTime(),
                "buy",
                symbol,
                amount,
                "",
                null,
                null,
                "RemoveLiquidityReport - Buy"
        ).toArray(new String[0]);
    }
}
