package com.ruho.rsk.domain;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class RskPaginationData implements Serializable {

    private static final long serialVersionUID = 4858398693222871441L;

    @SerializedName("has_more")
    private Boolean hasMore;

    @SerializedName("page_number")
    private Long pageNumber;

    @SerializedName("page_size")
    private Long pageSize;

    @SerializedName("total_count")
    private Long totalCount;

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(final Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final Long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskPaginationData that = (RskPaginationData) o;
        return Objects.equals(hasMore, that.hasMore)
                && Objects.equals(pageNumber, that.pageNumber)
                && Objects.equals(pageSize, that.pageSize)
                && Objects.equals(totalCount, that.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasMore, pageNumber, pageSize, totalCount);
    }
}

