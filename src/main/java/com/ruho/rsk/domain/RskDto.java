package com.ruho.rsk.domain;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class RskDto implements Serializable {

    private static final long serialVersionUID = 3644498029442069605L;

    private RskData data;
    private boolean error;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("error_code")
    private String errorCode;

    public RskData getData() {
        return data;
    }

    public void setData(final RskData data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskDto rskDto = (RskDto) o;
        return error == rskDto.error && Objects.equals(data, rskDto.data)
                && Objects.equals(errorMessage, rskDto.errorMessage)
                && Objects.equals(errorCode, rskDto.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, error, errorMessage, errorCode);
    }

}
