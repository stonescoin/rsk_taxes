package com.ruho.rsk.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class RskValueObject implements Serializable {

    private static final long serialVersionUID = 273367480078638302L;

    private String value;
    private Integer bitSize;
    private String typeAsString;

    public RskValueObject() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Integer getBitSize() {
        return bitSize;
    }

    public void setBitSize(final Integer bitSize) {
        this.bitSize = bitSize;
    }

    public String getTypeAsString() {
        return typeAsString;
    }

    public void setTypeAsString(final String typeAsString) {
        this.typeAsString = typeAsString;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskValueObject that = (RskValueObject) o;
        return Objects.equals(value, that.value)
                && Objects.equals(bitSize, that.bitSize)
                && Objects.equals(typeAsString, that.typeAsString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, bitSize, typeAsString);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RskValueObject.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .add("bitSize=" + bitSize)
                .add("typeAsString='" + typeAsString + "'")
                .toString();
    }
}
