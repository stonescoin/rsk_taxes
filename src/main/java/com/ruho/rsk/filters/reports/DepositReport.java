package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;

import java.math.BigDecimal;

public class DepositReport extends AbstractReport<DepositReport>{
    private String symbol;
    private BigDecimal amount;

    public String getSymbol() {
        return symbol;
    }

    public DepositReport setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DepositReport setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.DEPOSIT;
    }
}
