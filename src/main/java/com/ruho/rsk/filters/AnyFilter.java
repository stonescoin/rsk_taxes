package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;

public interface AnyFilter {
    AnyReport generateReport(RskItem transaction);

    boolean isTransactionInteresting(RskItem item);
}
