package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AddLiquidityReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.PoolContractSpecs;
import com.ruho.rsk.utils.NumberParser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static com.ruho.rsk.steps.StepsFilter.*;

@Component
public class AddLiquidityFilter implements AnyFilter {
    public AddLiquidityReport generateReport(RskItem transaction) {
        String contractAddress = findContractAddress(transaction);
        RskLogEvent liquidityAddedEvent = findLiquidityAddedEvent(transaction);
        BigDecimal poolTokensAmount = findAmountParam(liquidityAddedEvent, "_poolTokenAmount");
        return PoolContractSpecs.findSpecsFromContract(contractAddress)
                .map(poolContractSpecs -> {
                    BigDecimal baseAmount = getTransferAmount(transaction, poolContractSpecs.getBaseSymbol());
                    BigDecimal quoteAmount = getTransferAmount(transaction, poolContractSpecs.getQuoteSymbol());
                    return new AddLiquidityReport()
                            .setTransactionHash(transaction.getTransactionHash())
                            .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                            .setFees(transaction.getTotalFees())
                            .setBaseSymbol(poolContractSpecs.getBaseSymbol())
                            .setBaseAmount(baseAmount)
                            .setQuotedSymbol(poolContractSpecs.getQuoteSymbol())
                            .setQuotedAmount(quoteAmount)
                            .setPoolTokenAmount(poolTokensAmount);
                }).orElseThrow(() ->
                    new IllegalStateException("contractSpecs not found for: " + contractAddress)
                );
    }

    private BigDecimal getTransferAmount(RskItem transaction, String symbol) {
        RskLogEvent transferEvent = findTransferEventBySymbol(transaction, symbol);
        return findFirstParam(transferEvent, "value")
                .map(params -> NumberParser.numberFrom(params.getValue(), transferEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("can't find value param in baseTransfer for " + transaction.getTransactionHash()));
    }

    private RskLogEvent findTransferEventBySymbol(RskItem transaction, String symbol) {
        List<RskLogEvent> transferEvents = findTransferEvents(transaction, symbol);
        Collections.reverse(transferEvents);
        return transferEvents.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find transferEvent by " + symbol + " for " + transaction.getTransactionHash()));
    }

    private String findContractAddress(RskItem transaction) {
        RskLogEvent issuanceEvent = findIssuanceEvent(transaction);
        return issuanceEvent.getSenderAddress();
    }

    @Override
    public boolean isTransactionInteresting(RskItem transaction) {
        if(transaction.getLogEvents() == null) {
            return false;
        }
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isAddLiquidity);
    }


}
