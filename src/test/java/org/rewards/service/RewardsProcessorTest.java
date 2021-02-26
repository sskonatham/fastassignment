package org.rewards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

class RewardsProcessorTest {

    static final String TEST_INPUT = "              [\n" +
            "            {\n" +
            "              \"name\": \"Superman\",\n" +
            "              \"transaction_date\": \"2021-01-22\",\n" +
            "              \"amount\": 40\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Superman\",\n" +
            "              \"transaction_date\": \"2020-12-12\",\n" +
            "              \"amount\": 60\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\": \"Superman\",\n" +
            "              \"transaction_date\": \"2020-11-16\",\n" +
            "              \"amount\": 120\n" +
            "            }\n" +
            "            ]";


    @Test
    void testParseOnce() throws JsonProcessingException {
        RewardsProcessor p = new RewardsProcessor(TEST_INPUT);
        Map<String, BigDecimal> result = p.getSummary().get("Superman");
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(0, new BigDecimal("90").compareTo(result.get("2020-11")));
        Assertions.assertEquals(0, new BigDecimal("10").compareTo(result.get("2020-12")));
        Assertions.assertEquals(0, new BigDecimal("0").compareTo(result.get("2021-01")));

    }

    @Test
    void testParseTwice() throws JsonProcessingException {
        RewardsProcessor p = new RewardsProcessor(TEST_INPUT);
        p.processData(TEST_INPUT);
        Map<String, BigDecimal> result = p.getSummary().get("Superman");
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(0, new BigDecimal("180").compareTo(result.get("2020-11")));
        Assertions.assertEquals(0, new BigDecimal("20").compareTo(result.get("2020-12")));
        Assertions.assertEquals(0, new BigDecimal("0").compareTo(result.get("2021-01")));

    }


    @Test
    void testParseThrice() throws JsonProcessingException {
        RewardsProcessor p = new RewardsProcessor(TEST_INPUT);
        p.processData(TEST_INPUT);
        p.processData(TEST_INPUT);
        Map<String, BigDecimal> result = p.getSummary().get("Superman");
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(0, new BigDecimal("270").compareTo(result.get("2020-11")));
        Assertions.assertEquals(0, new BigDecimal("30").compareTo(result.get("2020-12")));
        Assertions.assertEquals(0, new BigDecimal("0").compareTo(result.get("2021-01")));

    }

    @Test
    void testParseTwoPersons() throws JsonProcessingException {
        RewardsProcessor p = new RewardsProcessor(TEST_INPUT);
        p.processData(TEST_INPUT.replaceAll("Superman", "Spiderman"));
        Map<String, BigDecimal> result1 = p.getSummary().get("Superman");
        Map<String, BigDecimal> result2 = p.getSummary().get("Superman");
        Assertions.assertEquals(3, result1.size());
        Assertions.assertEquals(3, result2.size());
        Assertions.assertEquals(0, new BigDecimal("90").compareTo(result1.get("2020-11")));
        Assertions.assertEquals(0, new BigDecimal("10").compareTo(result1.get("2020-12")));
        Assertions.assertEquals(0, new BigDecimal("0").compareTo(result1.get("2021-01")));
        Assertions.assertEquals(0, new BigDecimal("90").compareTo(result2.get("2020-11")));
        Assertions.assertEquals(0, new BigDecimal("10").compareTo(result2.get("2020-12")));
        Assertions.assertEquals(0, new BigDecimal("0").compareTo(result2.get("2021-01")));
    }
}
