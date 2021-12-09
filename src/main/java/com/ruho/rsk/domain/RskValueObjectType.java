package com.ruho.rsk.domain;

public enum RskValueObjectType {

    ADDRESS("address"),
    uint256("uint256");

    private final String type;
    RskValueObjectType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
