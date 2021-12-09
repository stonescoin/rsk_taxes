package com.ruho.rsk.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class RskLogEvent implements Serializable {

    private static final long serialVersionUID = -1180550712903615406L;

    @SerializedName("block_signed_at")
    private String blockSignedAt;

    @SerializedName("block_height")
    private Long blockHeight;

    @SerializedName("tx_offset")
    private Long transactionOffset;

    @SerializedName("log_offset")
    private Long logOffset;

    @SerializedName("tx_hash")
    private String transactionHash;

    @SerializedName("_raw_log_topics_bytes")
    private String rawLogTopicsBytes;

    @SerializedName("raw_log_topics")
    private List<String> rawLogTopics;

    @SerializedName("sender_contract_decimals")
    private Integer senderContractDecimals;

    @SerializedName("sender_name")
    private String senderName;

    @SerializedName("sender_contract_ticker_symbol")
    private String senderContract_ticker_symbol;

    @SerializedName("sender_address")
    private String senderAddress;

    @SerializedName("sender_address_label")
    private String senderAddressLabel;

    @SerializedName("sender_logo_url")
    private String senderLogoUrl;

    @SerializedName("raw_log_data")
    private String rawLogData;

    private RskDecodedData decoded;

    public String getBlockSignedAt() {
        return blockSignedAt;
    }

    public void setBlockSignedAt(final String blockSignedAt) {
        this.blockSignedAt = blockSignedAt;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(final Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Long getTransactionOffset() {
        return transactionOffset;
    }

    public void setTransactionOffset(final Long transactionOffset) {
        this.transactionOffset = transactionOffset;
    }

    public Long getLogOffset() {
        return logOffset;
    }

    public void setLogOffset(final Long logOffset) {
        this.logOffset = logOffset;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getRawLogTopicsBytes() {
        return rawLogTopicsBytes;
    }

    public void setRawLogTopicsBytes(final String rawLogTopicsBytes) {
        this.rawLogTopicsBytes = rawLogTopicsBytes;
    }

    public List<String> getRawLogTopics() {
        return rawLogTopics;
    }

    public void setRawLogTopics(final List<String> rawLogTopics) {
        this.rawLogTopics = rawLogTopics;
    }

    public Integer getSenderContractDecimals() {
        return senderContractDecimals;
    }

    public void setSenderContractDecimals(Integer senderContractDecimals) {
        this.senderContractDecimals = senderContractDecimals;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }

    public String getSenderContract_ticker_symbol() {
        return senderContract_ticker_symbol;
    }

    public void setSenderContract_ticker_symbol(final String senderContract_ticker_symbol) {
        this.senderContract_ticker_symbol = senderContract_ticker_symbol;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(final String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderAddressLabel() {
        return senderAddressLabel;
    }

    public void setSenderAddressLabel(final String senderAddressLabel) {
        this.senderAddressLabel = senderAddressLabel;
    }

    public String getSenderLogoUrl() {
        return senderLogoUrl;
    }

    public void setSenderLogoUrl(final String senderLogoUrl) {
        this.senderLogoUrl = senderLogoUrl;
    }

    public String getRawLogData() {
        return rawLogData;
    }

    public void setRawLogData(final String rawLogData) {
        this.rawLogData = rawLogData;
    }

    public RskDecodedData getDecoded() {
        return decoded;
    }

    public void setDecoded(final RskDecodedData decoded) {
        this.decoded = decoded;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskLogEvent that = (RskLogEvent) o;
        return Objects.equals(blockSignedAt, that.blockSignedAt)
                && Objects.equals(blockHeight, that.blockHeight)
                && Objects.equals(transactionOffset, that.transactionOffset)
                && Objects.equals(logOffset, that.logOffset)
                && Objects.equals(transactionHash, that.transactionHash)
                && Objects.equals(rawLogTopicsBytes, that.rawLogTopicsBytes)
                && Objects.equals(rawLogTopics, that.rawLogTopics)
                && Objects.equals(senderContractDecimals, that.senderContractDecimals)
                && Objects.equals(senderName, that.senderName)
                && Objects.equals(senderContract_ticker_symbol, that.senderContract_ticker_symbol)
                && Objects.equals(senderAddress, that.senderAddress)
                && Objects.equals(senderAddressLabel, that.senderAddressLabel)
                && Objects.equals(senderLogoUrl, that.senderLogoUrl)
                && Objects.equals(rawLogData, that.rawLogData)
                && Objects.equals(decoded, that.decoded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockSignedAt, blockHeight, transactionOffset, logOffset, transactionHash, rawLogTopicsBytes, rawLogTopics,
                            senderContractDecimals, senderName, senderContract_ticker_symbol, senderAddress, senderAddressLabel,
                            senderLogoUrl,
                            rawLogData, decoded);
    }
}
