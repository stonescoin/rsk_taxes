package com.ruho.rsk.filters.reports;

import java.time.LocalDateTime;

public abstract class AbstractReport<SELF extends AbstractReport<? extends AnyReport>> implements AnyReport {
    private String transactionHash;
    private LocalDateTime time;

    @Override
    public String getTransactionHash() {
        return transactionHash;
    }

    public SELF setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
        return self();
    }


    public LocalDateTime getTime() {
        return time;
    }

    public SELF setTime(final LocalDateTime time) {
        this.time = time;
        return self();
    }

    private SELF self() {
        return (SELF) this;
    }

    @Override
    public String toString() {
        return "AbstractReport{" +
                "transactionHash='" + transactionHash + '\'' +
                ", time=" + time +
                '}';
    }
}
