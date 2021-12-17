package com.ruho.rsk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RskData implements Serializable {

    private static final long serialVersionUID = -3085234957876253455L;

    private String address;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("next_update_at")
    private String nextUpdateAt;

    @JsonProperty("quote_currency")
    private String quoteCurrency;

    @JsonProperty("chain_id")
    private Integer chainId;

    private RskPaginationData pagination;

    private List<RskItem> items;

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNextUpdateAt() {
        return nextUpdateAt;
    }

    public void setNextUpdateAt(final String nextUpdateAt) {
        this.nextUpdateAt = nextUpdateAt;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(final String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public Integer getChainId() {
        return chainId;
    }

    public void setChainId(final Integer chainId) {
        this.chainId = chainId;
    }

    public RskPaginationData getPagination() {
        return pagination;
    }

    public void setPagination(final RskPaginationData pagination) {
        this.pagination = pagination;
    }

    public List<RskItem> getItems() {
        return items;
    }

    public void setItems(final List<RskItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskData rskData = (RskData) o;
        return Objects.equals(address, rskData.address)
                && Objects.equals(updatedAt, rskData.updatedAt)
                && Objects.equals(nextUpdateAt, rskData.nextUpdateAt)
                && Objects.equals(quoteCurrency, rskData.quoteCurrency)
                && Objects.equals(chainId, rskData.chainId)
                && Objects.equals(pagination, rskData.pagination)
                && Objects.equals(items, rskData.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, updatedAt, nextUpdateAt, quoteCurrency, chainId, pagination, items);
    }
}
