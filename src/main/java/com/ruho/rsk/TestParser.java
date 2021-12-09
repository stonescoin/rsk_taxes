package com.ruho.rsk;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruho.rsk.domain.*;
import com.ruho.rsk.filters.RemoveLiquidityFilter;
import com.ruho.rsk.filters.RemoveLiquidityReport;

public class TestParser {

    private static final Type type = new TypeToken<List<RskValueObject>>(){}.getType();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        TestParser tp = new TestParser();

        tp.checkParser();
    }

    private void checkParser() {
        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = classLoader.getResource("rsk.json").getFile();

        try (Reader reader = new FileReader(filePath)) {

            RskDto dto = gson.fromJson(reader, RskDto.class);

            RemoveLiquidityFilter removeLiquidityFilter = new RemoveLiquidityFilter();

            for (RskItem transaction : dto.getData().getItems()) {
                if(removeLiquidityFilter.isTransactionInteresting(transaction)) {
                    try {
                        RemoveLiquidityReport removeLiquidityReport = removeLiquidityFilter.generateReport(transaction);
                        System.out.println(removeLiquidityReport);
                        System.out.println("------------------");
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
