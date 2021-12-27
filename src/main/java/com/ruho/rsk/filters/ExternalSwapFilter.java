package com.ruho.rsk.filters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.SpotSwapReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.NumberParser;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.ruho.rsk.steps.LogEventNames.TRANSFER;

@Component
public class ExternalSwapFilter implements AnyFilter {

    private final static String USER_CONTRACT_NAME = "user";
    private final static String SOURCE_TOKEN_NAME = "sourceToken";
    private final static String SOURCE_TOKEN_AMOUNT_NAME = "sourceAmount";
    private final static String DESTINATION_TOKEN_NAME = "destToken";
    private final static String DESTINATION_TOKEN_AMOUNT_NAME = "destAmount";

    private final static Set<String> EXTERNAL_SWAP_LOG_EVENTS = new HashSet<>(Arrays.asList(
            USER_CONTRACT_NAME,
            SOURCE_TOKEN_NAME,
            SOURCE_TOKEN_AMOUNT_NAME,
            DESTINATION_TOKEN_NAME,
            DESTINATION_TOKEN_AMOUNT_NAME));

    @Override
    public AnyReport generateReport(final RskItem transaction) {
        SwapData swapData = findExternalSwapData(transaction);

        //todo assert if not user/owner

        Integer sourceDecimal = findContractDecimal(transaction, swapData.getSourceContract());
        BigDecimal sourceAmount = NumberParser.numberFrom(swapData.getSourceAmount(), sourceDecimal);

        Integer destinationDecimal = findContractDecimal(transaction, swapData.getDestinationContract());
        BigDecimal destinationAmount = NumberParser.numberFrom(swapData.getDestinationAmount(), destinationDecimal);

        return new SpotSwapReport()
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setSourceSymbol(TokenContractSpecs.findByAddress(swapData.getSourceContract()).getSymbol())
                .setSourceAmount(sourceAmount)
                .setTargetSymbol(TokenContractSpecs.findByAddress(swapData.getDestinationContract()).getSymbol())
                .setTargetAmount(destinationAmount);
    }

    private Integer findContractDecimal(RskItem transaction, String sourceContract) {
        return transaction.getLogEvents()
                .stream()
                .filter(rskLogEvent -> rskLogEvent.getDecoded() != null)
                .filter(rskLogEvent -> TRANSFER.equals(rskLogEvent.getDecoded().getName()))
                .filter(rskLogEvent -> sourceContract.equals(rskLogEvent.getSenderAddress()))
                .findFirst()
                .map(RskLogEvent::getSenderContractDecimals)
                .orElseThrow(() -> new IllegalStateException("For external swap approval must exist. Tr_Hash: " + transaction.getTransactionHash()));
    }

    private SwapData findExternalSwapData(final RskItem transaction) {
        List<RskDecodedData.Param> params = transaction.getLogEvents().stream()
                .filter(StepsFilter::isExternalSwap)
                .flatMap(rskLogEvent -> rskLogEvent.getDecoded().getParams()
                        .stream().filter(this::isExternalSwapLogEvent))
                .collect(Collectors.toList());

        String userContract = tryFindValue(transaction, params, USER_CONTRACT_NAME);

        String sourceTokenContract = tryFindValue(transaction, params, SOURCE_TOKEN_NAME);
        String sourceAmount = tryFindValue(transaction, params, SOURCE_TOKEN_AMOUNT_NAME);

        String destinationTokenContract = tryFindValue(transaction, params, DESTINATION_TOKEN_NAME);
        String destinationAmount = tryFindValue(transaction, params, DESTINATION_TOKEN_AMOUNT_NAME);

        return new SwapData(userContract, sourceTokenContract, sourceAmount, destinationTokenContract, destinationAmount);
    }

    private String tryFindValue(RskItem transaction, List<RskDecodedData.Param> params, String name) {
        return params.stream()
                .filter(param -> name.equals(param.getName()))
                .map(param -> param.getValue().toString())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find " + name + " for tx_hash: " + transaction.getTransactionHash()));
    }

    private boolean isExternalSwapLogEvent(final RskDecodedData.Param param) {
        return StringUtils.hasText(param.getName()) && EXTERNAL_SWAP_LOG_EVENTS.contains(param.getName());
    }

    @Override
    public boolean isTransactionInteresting(final RskItem transaction) {
        return transaction.getLogEvents().stream()
                .anyMatch(StepsFilter::isExternalSwap);
    }

    private static class SwapData {
        private String userContract;

        private String sourceContract;
        private String sourceAmount;

        private String destinationContract;
        private String destinationAmount;

        public SwapData(String userContract,
                String sourceContract,
                String sourceAmount,
                String destinationContract,
                String destinationAmount
        ) {
            this.userContract = userContract;
            this.sourceContract = sourceContract;
            this.sourceAmount = sourceAmount;
            this.destinationContract = destinationContract;
            this

                    .destinationAmount = destinationAmount;
        }

        public String getUserContract() {
            return userContract;
        }

        public String getSourceContract() {
            return sourceContract;
        }

        public String getSourceAmount() {
            return sourceAmount;
        }

        public String getDestinationContract() {
            return destinationContract;
        }

        public String getDestinationAmount() {
            return destinationAmount;
        }
    }
}
