package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.ContractSpecs;
import com.ruho.rsk.utils.NumberParser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static com.ruho.rsk.steps.StepsFilter.*;

public class RemoveLiquidityFilter implements AnyFilter {
    @Override
    public RemoveLiquidityReport generateReport(RskItem transaction) {
        String contractAddress = findContractAddress(transaction);
        return ContractSpecs.findSpecsFromContract(contractAddress)
                .map(contractSpecs -> {
                    BigDecimal baseAmount = getTransferAmount(transaction,  contractSpecs.getBaseSymbol());
                    BigDecimal quoteAmount = getTransferAmount(transaction,  contractSpecs.getQuoteSymbol());
                    return new RemoveLiquidityReport()
                            .setTransactionHash(transaction.getTransactionHash())
                            .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                            .setFees(transaction.getTotalFees())
                            .setSovsRewards(findRewardsAmount(transaction))
                            .setBaseSymbol(contractSpecs.getBaseSymbol())
                            .setBaseAmount(baseAmount)
                            .setQuotedSymbol(contractSpecs.getQuoteSymbol())
                            .setQuotedAmount(quoteAmount);
                }).orElseThrow(() ->
                    new IllegalStateException("contractSpecs not found for: " + contractAddress)
                );
    }

    private BigDecimal getTransferAmount(RskItem transaction, String symbol) {
        RskLogEvent transferEvent = findTransferEventBySymbol(transaction, symbol);
        return findFirstParam(transferEvent, "value")
                .map(param -> NumberParser.numberFrom(param.getValue(), transferEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("can't find value param in baseTransfer for " + transaction.getTransactionHash()));
    }

    private RskLogEvent findTransferEventBySymbol(RskItem transaction, String symbol) {
        return findTransferEvents(transaction, symbol).stream()
                .filter(logEvent -> logEvent.getSenderContract_ticker_symbol().contains(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find transferEvent by " + symbol + " for " + transaction.getTransactionHash()));
    }

    private String findContractAddress(RskItem transaction) {
        RskLogEvent withdrawEvent = findWithdrawEvent(transaction);
        return String.valueOf(findFirstParam(withdrawEvent, "recipient")
                .map(RskDecodedData.Param::getValue)
                .orElseThrow(() -> new IllegalStateException("recipient param not found for " + withdrawEvent.getTransactionHash() + "_" + withdrawEvent.getLogOffset())));
    }

    private BigDecimal findRewardsAmount(RskItem transaction) {
        List<RskLogEvent> transferEvents = findTransferEvents(transaction, "SOV");
        Collections.reverse(transferEvents);
        RskLogEvent rewardsEvent = transferEvents.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find Transfer rewards event"));
        return findFirstParam(rewardsEvent, "value")
                .map(RskDecodedData.Params::getValue)
                .map(number -> NumberParser.numberFrom(number, rewardsEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("value param not found for hash: " + rewardsEvent.getTransactionHash() + " offset: " + rewardsEvent.getLogOffset()));
    }


    @Override
    public boolean isTransactionInteresting(RskItem transaction) {
        if(transaction.getLogEvents() == null) {
            return false;
        }
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isRemoveLiquidity);
    }


}
