package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;

public interface AnyFilter {
    boolean isTransactionInteresting(RskItem item);
}
