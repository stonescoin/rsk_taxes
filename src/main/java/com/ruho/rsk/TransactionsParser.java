package com.ruho.rsk;

import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.filters.AddLiquidityFilter;
import com.ruho.rsk.filters.AnyFilter;
import com.ruho.rsk.filters.RemoveLiquidityFilter;
import com.ruho.rsk.filters.SpotSwapFilter;
import com.ruho.rsk.filters.reports.AnyReport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsParser {

    private final AnyFilter[] allFilters = new AnyFilter[]{
            new RemoveLiquidityFilter(),
            new AddLiquidityFilter(),
            new SpotSwapFilter()
    };

    public List<AnyReport> parse(RskDto dto) {
        return dto.getData().getItems().stream()
                .flatMap(transaction ->
                    Arrays.stream(allFilters)
                            .filter(anyFilter -> anyFilter.isTransactionInteresting(transaction))
                            .map(anyFilter -> anyFilter.generateReport(transaction))
                            .findFirst().stream()
                )
                .collect(Collectors.toList());
    }

}
