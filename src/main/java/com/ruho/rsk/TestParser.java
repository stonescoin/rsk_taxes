package com.ruho.rsk;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruho.rsk.domain.*;
import com.ruho.rsk.filters.AddLiquidityFilter;
import com.ruho.rsk.filters.AnyFilter;
import com.ruho.rsk.filters.RemoveLiquidityFilter;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;

public class TestParser {

    private static final Gson gson = new Gson();

    private final AnyFilter[] allFilters = new AnyFilter[]{
            new RemoveLiquidityFilter(),
            new AddLiquidityFilter()
    };

    public static void main(String[] args) {
        TestParser tp = new TestParser();

        tp.checkParser();
    }

    private void checkParser() {
        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = classLoader.getResource("rsk.json").getFile();

        try (Reader reader = new FileReader(filePath)) {

            RskDto dto = gson.fromJson(reader, RskDto.class);

            for (RskItem transaction : dto.getData().getItems()) {
                AnyReport report;
                    for (AnyFilter filter : allFilters) {
                        try {
                            if(filter.isTransactionInteresting(transaction)) {
                                report = filter.generateReport(transaction);
                                System.out.println(report);
                                System.out.println("------------------");
                            }
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                            System.err.println(e.getMessage());
                        }
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
