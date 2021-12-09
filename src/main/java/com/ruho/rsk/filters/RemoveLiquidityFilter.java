package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.ContractSpecs;
import com.ruho.rsk.utils.NumberParser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruho.rsk.steps.StepsFilter.*;

public class RemoveLiquidityFilter implements AnyFilter {
    public RemoveLiquidityReport generateReport(RskItem transaction) {
        RskLogEvent rewardsClaimedEvent = findRewardsClaimedEvent(transaction);
        String contractAddress = findContractAddress(transaction);
        return ContractSpecs.findSpecsFromContract(contractAddress)
                .map(contractSpecs -> {
                    BigDecimal baseAmount = getTransferAmount(transaction,  contractSpecs.getBaseSymbol());
                    BigDecimal quoteAmount = getTransferAmount(transaction,  contractSpecs.getQuoteSymbol());

                    RemoveLiquidityReport report = new RemoveLiquidityReport()
                            .setTransactionHash(transaction.getTransactionHash())
                            .setFees(transaction.getTotalFees())
                            .setSovsRewards(findRewardsAmount(rewardsClaimedEvent))
                            .setBaseSymbol(contractSpecs.getBaseSymbol())
                            .setBaseAmount(baseAmount)
                            .setQuotedSymbol(contractSpecs.getQuoteSymbol())
                            .setQuotedAmount(quoteAmount);

                    return report;
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
        return findTransferEvents(transaction).stream()
                .filter(logEvent -> logEvent.getSenderContract_ticker_symbol().contains(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find transferEvent by " + symbol + " for " + transaction.getTransactionHash()));
    }

    private String findContractAddress(RskItem transaction) {
        RskLogEvent withdrawEvent = findWithdrawEvent(transaction);
        return String.valueOf(findFirstParam(withdrawEvent, "recipient")
                .map(RskDecodedData.Params::getValue)
                .orElseThrow(() -> new IllegalStateException("recipient param not found for " + withdrawEvent.getTransactionHash() + "_" + withdrawEvent.getLogOffset())));
    }

    private BigDecimal findRewardsAmount(RskLogEvent rewardsClaimedEvent) {
        return findFirstParam(rewardsClaimedEvent, "amount")
                .map(RskDecodedData.Params::getValue)
                .map(number -> NumberParser.numberFrom(number, rewardsClaimedEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("amount param not found for " + rewardsClaimedEvent.getTransactionHash() + "_" + rewardsClaimedEvent.getLogOffset()));
    }


    @Override
    public boolean isTransactionInteresting(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isRemoveLiquidity);
    }


}
