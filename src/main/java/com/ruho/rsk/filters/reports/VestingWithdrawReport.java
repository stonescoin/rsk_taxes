package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;

import java.math.BigDecimal;

public class VestingWithdrawReport extends AbstractReport<VestingWithdrawReport> {

    private String symbol;            // usdt ?
    private BigDecimal amount;

    private BigDecimal fees;    //always in BTC


    public String getSymbol() {
        return symbol;
    }

    public VestingWithdrawReport setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public VestingWithdrawReport setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public VestingWithdrawReport setFees(final BigDecimal fees) {
        this.fees = fees;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SPOT_SWAP;
    }

    @Override
    public String toString() {
        return "VestingWithdrawReport{" +
                "symbol='" + symbol + '\'' +
                ", amount=" + amount +
                ", fees=" + fees +
                "} " + super.toString();
    }
}
