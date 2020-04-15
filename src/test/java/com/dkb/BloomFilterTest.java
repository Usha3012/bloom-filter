package com.dkb;


import com.dkb.exception.InvalidInputException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class BloomFilterTest {

    @Test
    @DisplayName("When_value_is_available_return_true")
    public void when_value_present_say_present() {
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("MightSessions");
        bloomFilter.add("moreFancy");
        assertAll("TruePositive",
                () -> assertTrue(bloomFilter.contains("MightSessions")),
                () -> assertTrue(bloomFilter.contains("moreFancy")));
    }

    @Test
    @DisplayName("When_value_is_not_available_return_false")
    public void when_value_not_present_say_not_present() {
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("Ragnarock");
        bloomFilter.add("Batman");
        assertAll("TruePositive",
                () -> assertFalse(bloomFilter.contains("MightSessions")),
                () -> assertFalse(bloomFilter.contains("moreFancy")));
    }

    @Test
    @DisplayName("When_value_is_available_return_true_with_custom_bloom")
    public void when_using_custom_constructor_filter_works() {
        List<String> tokens = IntStream.range(0, 10000)
                .mapToObj(i -> RandomStringUtils.randomAlphanumeric(5, 10))
                .collect(Collectors.toList());


        BloomFilter bloomFilter = new BloomFilter(10000, 0.005);
        tokens.stream()
                .forEach(bloomFilter::add);

        tokens.forEach(token -> assertTrue(bloomFilter.contains(token)));

    }

    @Test
    @DisplayName("Invalid_Token_Not_Allowed")
    public void token_should_not_be_null() {
        BloomFilter bloomFilter = new BloomFilter();
        assertAll("InvalidTokens",
                () -> assertThrows(InvalidInputException.class, () -> bloomFilter.contains(null)),
                () -> assertThrows(InvalidInputException.class, () -> bloomFilter.contains(StringUtils.EMPTY)),
                () -> assertThrows(InvalidInputException.class, () -> bloomFilter.contains(StringUtils.SPACE))
        );

    }
}
