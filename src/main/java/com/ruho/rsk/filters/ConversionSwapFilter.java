package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.SpotSwapReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class ConversionSwapFilter implements AnyFilter {

    @Override
    public AnyReport generateReport(final RskItem transaction) {
        List<RskLogEvent> transferEvents = StepsFilter.findTransferEvents(transaction);
        RskLogEvent targetTransfer = transferEvents.stream().iterator().next();
        RskLogEvent sourceTransfer = transferEvents.get(transferEvents.size() - 1);

        BigDecimal fromAmount = StepsFilter.findAmountParam(sourceTransfer);
        BigDecimal toAmount = StepsFilter.findAmountParam(targetTransfer);

        return new SpotSwapReport()
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setSourceSymbol(TokenContractSpecs.findByAddress(sourceTransfer.getSenderAddress()).getSymbol())
                .setSourceAmount(fromAmount)
                .setTargetSymbol(TokenContractSpecs.findByAddress(targetTransfer.getSenderAddress()).getSymbol())
                .setTargetAmount(toAmount);
    }

    @Override
    public boolean isTransactionInteresting(final RskItem transaction) {
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isConversionSwap) &&
                StepsFilter.findTransferEvents(transaction).size() > 1;
    }
}
