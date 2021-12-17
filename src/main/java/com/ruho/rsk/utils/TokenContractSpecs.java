package com.ruho.rsk.utils;

import java.util.Arrays;

/**
 * Stores all tokens contract
 */
public enum TokenContractSpecs {
    SOV("0xefc78fc7d48b64958315949279ba181c2114abbd", "SOV"),
    BTC("0x542fda317318ebf1d3deaf76e0b632741a7e677d", "BTC"),
    USD("0xb5999795be0ebb5bab23144aa5fd6a02d080299f", "USD"),
    ETH("0x1d931bf8656d795e50ef6d639562c5bd8ac2b78f", "ETH");

    private final String address;
    private final String symbol;

    TokenContractSpecs(String address, String symbol) {
        this.address = address;
        this.symbol = symbol;
    }

    public String getAddress() {
        return address;
    }

    public String getSymbol() {
        return symbol;
    }

    public static TokenContractSpecs findByAddress(String address) {
        return Arrays.stream(values())
                .filter(tokenContractSpecs -> tokenContractSpecs.getAddress().equals(address))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find TokenContract for address: " + address));
    }

    public static boolean isValidContract(String address) {
        return Arrays.stream(values())
                .anyMatch(tokenContractSpecs -> tokenContractSpecs.getAddress().equals(address));
    }
}
