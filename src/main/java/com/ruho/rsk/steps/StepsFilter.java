package com.ruho.rsk.steps;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruho.rsk.steps.LogEventNames.*;

public class StepsFilter {
    public static boolean isRemoveLiquidity(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, LIQUIDITY_REMOVED_V1);
    }

    public static boolean isAddLiquidity(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, LIQUIDITY_ADDED_V1);
    }

    private static boolean isLogEventNamed(RskLogEvent logEvent, String name) {
        if(logEvent.getDecoded() == null) {
            return false;
        }
        if(!StringUtils.hasText(logEvent.getDecoded().getName())) {
            throw new IllegalStateException("decodedData#name is empty");
        }
        return logEvent.getDecoded().getName().equals(name);
    }

    public static boolean isRewardsClaimed(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(REWARDS_CLAIMED);
    }

    public static boolean isTransfer(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(TRANSFER);
    }

    public static List<RskLogEvent> findTransferEvents(RskItem transaction, String symbol) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isTransfer)
                .filter(logEvent -> logEvent.getSenderContract_ticker_symbol().contains(symbol))
                .collect(Collectors.toList());
    }

    public static RskLogEvent findWithdrawEvent(RskItem transaction) {
        return findFirstEvent(transaction, WITHDRAW);
    }

    public static RskLogEvent findIssuanceEvent(RskItem transaction) {
        return findFirstEvent(transaction, ISSUANCE);
    }

    public static RskLogEvent findRewardsClaimedEvent(RskItem transaction) {
        return findFirstEvent(transaction, REWARDS_CLAIMED);
    }

    private static RskLogEvent findFirstEvent(RskItem transaction, String name) {
        return transaction.getLogEvents().stream()
                .filter(logEvent -> logEvent.getDecoded().getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no " + name + " event found for " + transaction.getTransactionHash()));
    }

    public static Optional<RskDecodedData.Param> findFirstParam(RskLogEvent event, String paramName) {
        if(event.getDecoded() == null || event.getDecoded().getParams() == null) {
            return Optional.empty();
        } else {
            return event.getDecoded().getParams().stream()
                    .filter(param -> param.getName().equals(paramName))
                    .findFirst();
        }
    }

    public static boolean isSpotSwap(RskLogEvent logEvent) {
        return isLogEventNamed(logEvent, SPOT_SWAP);
    }

}
