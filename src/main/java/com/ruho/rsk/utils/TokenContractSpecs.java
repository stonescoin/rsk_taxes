package com.ruho.rsk.utils;

import java.util.Arrays;

/**
 * Stores all tokens contract
 */
public enum TokenContractSpecs {
    SOV("0xefc78fc7d48b64958315949279ba181c2114abbd", "SOV"),
    MYNT("0x2e6b1d146064613e8f521eb3c6e65070af964ebb", "MYNT"),
    BTC("0x542fda317318ebf1d3deaf76e0b632741a7e677d", "BTC"),
    USD("0xb5999795be0ebb5bab23144aa5fd6a02d080299f", "USD"),
    FISH("0x055a902303746382fbb7d18f6ae0df56efdc5213", "FISH"),
    BNB("0x6d9659bdf5b1a1da217f7bbaf7dbaf8190e2e71b", "BNB"),
    ETH("0x1d931bf8656d795e50ef6d639562c5bd8ac2b78f", "ETH"),
    RUSDT("0xef213441a85df4d7acbdae0cf78004e1e486bb96", "RUSDT");

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
