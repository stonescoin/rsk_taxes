package com.ruho.rsk.filters.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ruho.rsk.filters.TransactionType;

public class SpotExternalSwapReport implements AnyReport {

    private String transactionHash;

    private String sourceSymbol;            // usdt ?
    private BigDecimal sourceAmount;

    private String targetSymbol;              // btc ?
    private BigDecimal targetAmount;

    private BigDecimal fees;    //always in BTC

    private LocalDateTime time;

    public String getTransactionHash() {
        return transactionHash;
    }

    public SpotExternalSwapReport setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
        return this;
    }

    public String getSourceSymbol() {
        return sourceSymbol;
    }

    public SpotExternalSwapReport setSourceSymbol(final String sourceSymbol) {
        this.sourceSymbol = sourceSymbol;
        return this;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public SpotExternalSwapReport setSourceAmount(final BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public SpotExternalSwapReport setTargetSymbol(final String targetSymbol) {
        this.targetSymbol = targetSymbol;
        return this;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public SpotExternalSwapReport setTargetAmount(final BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public SpotExternalSwapReport setFees(final BigDecimal fees) {
        this.fees = fees;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public SpotExternalSwapReport setTime(final LocalDateTime time) {
        this.time = time;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SPOT_SWAP;
    }

    @Override
    public String toString() {
        return "SpotSwapReport{" +
                "transactionHash='" + transactionHash + '\'' +
                ", sourceSymbol='" + sourceSymbol + '\'' +
                ", sourceAmount=" + sourceAmount +
                ", targetSymbol='" + targetSymbol + '\'' +
                ", targetAmount=" + targetAmount +
                ", fees=" + fees +
                ", time=" + time +
                '}';
    }
}
