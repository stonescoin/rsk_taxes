package com.ruho.rsk.generators;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CTCPrinter {

    public static List<String> printToList(LocalDateTime timestamp, String type, String baseCurrency, BigDecimal baseAmount, String quoteCurrency, BigDecimal quoteAmount, BigDecimal feeAmount, String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> parts = new ArrayList<>();
        parts.add(formatter.format(timestamp));
        parts.add(type);
        parts.add(baseCurrency);
        parts.add(baseAmount.toPlainString());
        parts.add(quoteCurrency);
        parts.add(quoteAmount == null ? null : quoteAmount.toPlainString());
        parts.add("BTC");
        parts.add(feeAmount == null ? null : feeAmount.toPlainString());
        parts.add(" ");
        parts.add("");
        parts.add(description);
        return parts;
    }
}
