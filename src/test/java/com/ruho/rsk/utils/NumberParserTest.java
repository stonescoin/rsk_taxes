package com.ruho.rsk.utils;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberParserTest {
    @Test
    public void testDecodingStringNumber() {
        assertThat(NumberParser.numberFrom("60000000")).isEqualByComparingTo(new BigDecimal("0.00000000006"));
    }
}
