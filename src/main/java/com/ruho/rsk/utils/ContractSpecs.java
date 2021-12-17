package com.ruho.rsk.utils;

import java.util.Arrays;
import java.util.Optional;

public enum ContractSpecs {
    BTC_XUSD_POOL("0x6f96096687952349dd5944e0eb1be327dcdeb705", "BTC", "XUSD"),
    BTC_ETH_POOL("0xf41ed702df2b84ace02772c6a0d8ae46465aa5f4", "BTC", "ETH"),
    BTC_BNB_POOL("0x8f3d24ab3510294f1466aa105f78901b90d79d4d", "BTC", "BNB");
    BTC_SOV_POOL("0x09c5faf7723b13434abdf1a65ab1b667bc02a902", "BTC", "SOV"),
    BTC_FISH_POOL("0x35a74a38fd7728f1c6bc39ae3b18c974b7979ddd", "BTC", "FISH");
    SOV("0xefc78fc7d48b64958315949279ba181c2114abbd", "SOV", "SOV", false),
    BTC("0x542fda317318ebf1d3deaf76e0b632741a7e677d", "BTC", "BTC", false),
    USD("0xb5999795be0ebb5bab23144aa5fd6a02d080299f", "USD", "USD", false),
    ETH("0x1d931bf8656d795e50ef6d639562c5bd8ac2b78f", "ETH", "ETH", false);


    private final String quoteSymbol;
    private final String contractAddress;
    private final String baseSymbol;
    private final boolean isPoolContract;

    ContractSpecs(String contractAddress, String baseSymbol, String quoteSymbol, boolean isPoolContract) {
        this.contractAddress = contractAddress;
        this.baseSymbol = baseSymbol;
        this.quoteSymbol = quoteSymbol;
        this.isPoolContract = isPoolContract;
    }

    public String getQuoteSymbol() {
        return quoteSymbol;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getBaseSymbol() {
        return baseSymbol;
    }

    public boolean isPoolContract() {
        return isPoolContract;
    }

    public static Optional<ContractSpecs> findSpecsFromContract(String contractAddress) {
        return Arrays.stream(values())
                .filter(contractSpecs -> contractSpecs.getContractAddress().equals(contractAddress))
                .findFirst();
    }

    public static boolean tryFindContract(String contractAddress, boolean isPoolContract) {
        return Arrays.stream(values())
                .filter(contractSpecs -> contractSpecs.getContractAddress().equals(contractAddress))
                .anyMatch(contractSpecs -> contractSpecs.isPoolContract() == isPoolContract);
    }
}
