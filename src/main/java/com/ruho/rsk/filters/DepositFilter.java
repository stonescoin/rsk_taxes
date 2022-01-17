package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.DepositReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DepositFilter implements AnyFilter {


    @Override
    public AnyReport generateReport(final RskItem transaction) {
        RskLogEvent transferEvent = StepsFilter.findTransferEvents(transaction).iterator().next();
        TokenContractSpecs tokenSpecs = TokenContractSpecs.findByAddress(transferEvent.getSenderAddress());
        BigDecimal amount = StepsFilter.findAmountParam(transferEvent);
        return new DepositReport()
                .setTransactionHash(transaction.getTransactionHash())
                .setSymbol(tokenSpecs.getSymbol())
                .setAmount(amount)
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC));
    }

    @Override
    public boolean isTransactionInteresting(final RskItem transaction) {
        return transaction.getLogEvents().stream().anyMatch(StepsFilter::isMinted) &&
                transaction.getLogEvents().stream().anyMatch(StepsFilter::isExecuted)
                ;
    }
}
