package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.VestingWithdrawReport;
import com.ruho.rsk.steps.StepsFilter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.ruho.rsk.steps.LogEventNames.*;
import static com.ruho.rsk.steps.StepsFilter.findAmountParam;

@Component
public class VestingWithdrawFilter implements AnyFilter {
    @Override
    public VestingWithdrawReport generateReport(RskItem transaction) {
        RskLogEvent transferEvent = StepsFilter.findTransferEvents(transaction).stream()
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("can't find transfer event in vestingWithdrawFilter"));

        return new VestingWithdrawReport()
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setSymbol(transferEvent.getSenderContract_ticker_symbol())
                .setAmount(findAmountParam(transferEvent))
        ;
    }


    @Override
    public boolean isTransactionInteresting(RskItem transaction) {
        if(transaction.getLogEvents() == null) {
            return false;
        }
        return StepsFilter.findFirstEvent(transaction, TOKENS_WITHDRAWN).isPresent() &&
                StepsFilter.findFirstEvent(transaction, TRANSFER).isPresent() &&
                StepsFilter.findFirstEvent(transaction, DELEGATE_STAKE_CHANGED).isPresent();
    }

}
