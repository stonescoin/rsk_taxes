package com.ruho.rsk;

import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.filters.AnyFilter;
import com.ruho.rsk.filters.reports.AnyReport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionsParser {
    private final List<AnyFilter> allFilters;

    public TransactionsParser(List<AnyFilter> allFilters) {
        this.allFilters = allFilters;
    }

    public List<AnyReport> parse(RskDto dto) {
        return dto.getData().getItems().stream()
                .flatMap(transaction ->
                        allFilters.stream()
                            .filter(anyFilter -> anyFilter.isTransactionInteresting(transaction))
                            .map(anyFilter -> anyFilter.generateReport(transaction))
                            .findFirst().stream()
                )
                .collect(Collectors.toList());
    }

}
