package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddLiquidityReport extends AbstractReport<AddLiquidityReport> {
    private String quotedSymbol;            // usdt ?
    private BigDecimal quotedAmount;

    private String baseSymbol;              // btc ?
    private BigDecimal baseAmount;

    private BigDecimal poolTokenAmount;

    private BigDecimal fees;    //always in BTC


    public BigDecimal getPoolTokenAmount() {
        return poolTokenAmount;
    }

    public AddLiquidityReport setPoolTokenAmount(BigDecimal poolTokenAmount) {
        this.poolTokenAmount = poolTokenAmount;
        return this;
    }

    public String getQuotedSymbol() {
        return quotedSymbol;
    }

    public AddLiquidityReport setQuotedSymbol(String quotedSymbol) {
        this.quotedSymbol = quotedSymbol;
        return this;
    }

    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }

    public AddLiquidityReport setQuotedAmount(BigDecimal quotedAmount) {
        this.quotedAmount = quotedAmount;
        return this;
    }

    public String getBaseSymbol() {
        return baseSymbol;
    }

    public AddLiquidityReport setBaseSymbol(String baseSymbol) {
        this.baseSymbol = baseSymbol;
        return this;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public AddLiquidityReport setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public AddLiquidityReport setFees(BigDecimal fees) {
        this.fees = fees;
        return this;
    }

    public String getPoolTokenSymbol() {
        return this.getBaseSymbol() + "-" + this.getQuotedSymbol() + "-LP";
    }

    @Override
    public String toString() {
        return "AddLiquidityReport{" +
                ", quotedSymbol='" + quotedSymbol + '\'' +
                ", quotedAmount=" + quotedAmount +
                ", baseSymbol='" + baseSymbol + '\'' +
                ", baseAmount=" + baseAmount +
                ", fees=" + fees +
                "} " + super.toString();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.LIQUIDITY_ADD;
    }

}
