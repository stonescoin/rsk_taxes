package com.ruho.rsk.utils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Pool contracts
 */
public enum PoolContractSpecs {
    BTC_XUSD_POOL("0x6f96096687952349dd5944e0eb1be327dcdeb705", "BTC", "XUSD"),
    BTC_ETH_POOL("0xf41ed702df2b84ace02772c6a0d8ae46465aa5f4", "BTC", "ETH"),
    BTC_BNB_POOL("0x8f3d24ab3510294f1466aa105f78901b90d79d4d", "BTC", "BNB"),
    BTC_SOV_POOL("0x09c5faf7723b13434abdf1a65ab1b667bc02a902", "BTC", "SOV"),
    BTC_MYNT_POOL("0x36263ac99ecdcf1ab20513d580b7d8d32d3c439d", "BTC", "MYNT"),
    BTC_FISH_POOL("0x35a74a38fd7728f1c6bc39ae3b18c974b7979ddd", "BTC", "FISH");


    private final String quoteSymbol;
    private final String contractAddress;
    private final String baseSymbol;

    PoolContractSpecs(String contractAddress, String baseSymbol, String quoteSymbol) {
        this.contractAddress = contractAddress;
        this.baseSymbol = baseSymbol;
        this.quoteSymbol = quoteSymbol;
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

    public static Optional<PoolContractSpecs> findSpecsFromContract(String contractAddress) {
        return Arrays.stream(values())
                .filter(poolContractSpecs -> poolContractSpecs.getContractAddress().equals(contractAddress))
                .findFirst();
    }
}
