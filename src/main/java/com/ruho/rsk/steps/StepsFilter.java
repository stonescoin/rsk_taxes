package com.ruho.rsk.steps;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruho.rsk.steps.LogEventNames.*;

public class StepsFilter {
    public static boolean isRemoveLiquidity(RskLogEvent logEvent) {
        if(logEvent.getDecoded() == null) {
            return false;
        }
        if(!StringUtils.hasText(logEvent.getDecoded().getName())) {
            throw new IllegalStateException("decodedData#name is empty");
        }
        return logEvent.getDecoded().getName().equals(LIQUIDITY_REMOVED_V1);
    }

    public static boolean isRewardsClaimed(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(REWARDS_CLAIMED);
    }

    public static boolean isWithdraw(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(WITHDRAW);
    }

    public static boolean isTransfer(RskLogEvent logEvent) {
        return logEvent.getDecoded().getName().equals(TRANSFER);
    }

    public static List<RskLogEvent> findTransferEvents(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isTransfer)
                .collect(Collectors.toList());
    }

    public static RskLogEvent findWithdrawEvent(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isWithdraw)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no withdraw event found for " + transaction.getTransactionHash()));
    }

    public static RskLogEvent findRewardsClaimedEvent(RskItem transaction) {
        return transaction.getLogEvents().stream()
                .filter(StepsFilter::isRewardsClaimed)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no rewardsClaimed event found for " + transaction.getTransactionHash()));
    }

    public static Optional<RskDecodedData.Params> findFirstParam(RskLogEvent event, String paramName) {
        return event.getDecoded().getParams().stream()
                .filter(params -> params.getName().equals(paramName))
                .findFirst();
    }
}
