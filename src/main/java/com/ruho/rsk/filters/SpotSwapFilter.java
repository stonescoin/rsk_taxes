package com.ruho.rsk.filters;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.domain.RskValueObject;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.SpotSwapReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.NumberParser;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.util.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ruho.rsk.steps.LogEventNames.WITHDRAWAL;

public class SpotSwapFilter implements AnyFilter {

    private static final Type type = new TypeToken<List<RskValueObject>>(){}.getType();
    private static final Gson gson = new Gson();

    private final static String SOURCE_TOKEN_AMOUNT_NAME = "_sourceTokenAmount";
    private final static String TARGET_TOKEN_AMOUNT_NAME = "_targetTokenAmount";
    private final static String PATH_NAME = "_path";

    private final static Set<String> SPOT_SWAP_LOG_EVENTS = new HashSet<>(Arrays.asList(
            SOURCE_TOKEN_AMOUNT_NAME,
            TARGET_TOKEN_AMOUNT_NAME,
            PATH_NAME));

    @Override
    public AnyReport generateReport(final RskItem transaction) {
        List<RskDecodedData.Param> paramList = findSwapData(transaction);
        SwapContracts swapContracts = findContracts(transaction, paramList);
        Integer sourceDecimal = findContractDecimal(transaction, swapContracts.getSourceContract());
        BigDecimal sourceAmount = extractAmount(paramList, SOURCE_TOKEN_AMOUNT_NAME, sourceDecimal);

        Integer targetDecimal = findContractDecimal(transaction, swapContracts.getSourceContract());
        BigDecimal targetAmount = extractAmount(paramList, TARGET_TOKEN_AMOUNT_NAME, targetDecimal);

        return new SpotSwapReport()
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setSourceSymbol(TokenContractSpecs.findByAddress(swapContracts.getSourceContract()).getSymbol())
                .setSourceAmount(sourceAmount)
                .setTargetSymbol(TokenContractSpecs.findByAddress(swapContracts.getTargetContract()).getSymbol())
                .setTargetAmount(targetAmount);
    }

    private SwapContracts findContracts(RskItem transaction, List<RskDecodedData.Param> paramList) {

        List<RskLogEvent> withdraws = transaction.getLogEvents()
                .stream()
                .filter(rskLogEvent -> rskLogEvent.getDecoded() != null)
                .filter(rskLogEvent -> WITHDRAWAL.equals(rskLogEvent.getDecoded().getName()))
                .collect(Collectors.toList());
        checkArgument(withdraws.size() > 0, "For spot withdrawal must exist. Tr_Hash: " + transaction.getTransactionHash());
        RskDecodedData.Param pathParam = paramList.stream()
                .filter(param -> PATH_NAME.equals(param.getName()))
                .findFirst()
                .orElseThrow();

        if (pathParam.getValue() instanceof ArrayList) {
            List<RskValueObject> addresses = gson.fromJson(pathParam.getValue().toString(), type);
            addresses = addresses.stream()
                    .filter(rskValueObject -> TokenContractSpecs.isValidContract(rskValueObject.getValue()))
                    .collect(Collectors.toList());

            String targetContract = addresses.stream()
                    .map(RskValueObject::getValue)
                    .filter(contractAddress -> withdraws.stream()
                            .map(RskLogEvent::getSenderAddress)
                            .anyMatch(address -> address.equals(contractAddress)))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("can't find target contract for tr_hash: " + transaction.getTransactionHash()));

            String sourceContract = addresses.stream()
                    .map(RskValueObject::getValue)
                    .filter(contractAddress -> !contractAddress.equals(targetContract))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("can't find target contract for tr_hash: " + transaction.getTransactionHash()));

            return new SwapContracts(sourceContract, targetContract);
        }

        throw new IllegalStateException("Can't find decimal for transaction: " + transaction.getTransactionHash());
    }

    private Integer findContractDecimal(RskItem transaction,String contractAddress) {
        return transaction.getLogEvents()
                .stream()
                .filter(rskLogEvent -> rskLogEvent.getSenderAddress().equals(contractAddress))
                .findFirst()
                .map(RskLogEvent::getSenderContractDecimals)
                .orElseThrow(() -> new IllegalStateException("can't find decimal for tr_hash: " + transaction.getTransactionHash()));
    }

    private BigDecimal extractAmount(List<RskDecodedData.Param> paramList, String tokenAmountName, Integer contractDecimal) {
        return paramList.stream()
                .filter(param -> param.getName().equals(tokenAmountName))
                .findFirst()
                .map(param -> NumberParser.numberFrom(param.getValue(), contractDecimal))
                .orElseThrow(() -> new IllegalStateException("can't find param for tokenAmount " + tokenAmountName));
    }

    @Override
    public boolean isTransactionInteresting(final RskItem transaction) {
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isSpotSwap);
    }

    private List<RskDecodedData.Param> findSwapData(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isSpotSwap)
                .flatMap(rskLogEvent -> rskLogEvent.getDecoded().getParams()
                        .stream().filter(this::isSpotSwapLogEvent))
                .collect(Collectors.toList());
    }

    private boolean isSpotSwapLogEvent(final RskDecodedData.Param param) {
        return StringUtils.hasText(param.getName()) && SPOT_SWAP_LOG_EVENTS.contains(param.getName());
    }

    private static class SwapContracts {
        String sourceContract;
        String targetContract;

        public SwapContracts(String sourceContract, String targetContract) {
            this.sourceContract = sourceContract;
            this.targetContract = targetContract;
        }

        public String getSourceContract() {
            return sourceContract;
        }

        public String getTargetContract() {
            return targetContract;
        }
    }
}
