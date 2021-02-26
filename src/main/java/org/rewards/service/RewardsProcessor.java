/* Copyright 2019 Mentor Graphics Corporation
 *            All Rights Reserved
 *
 * THIS WORK CONTAINS TRADE SECRET
 * AND PROPRIETARY INFORMATION WHICH IS THE
 * PROPERTY OF MENTOR GRAPHICS
 * CORPORATION OR ITS LICENSORS AND IS
 * SUBJECT TO LICENSE TERMS.
 */
package org.rewards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rewards.entity.Transaction;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class RewardsProcessor {

    static BigDecimal FIFTY = new BigDecimal("50.00");

    SimpleDateFormat PATTERN_YEAR_MONTH = new SimpleDateFormat("yyyy-MM");

    private Map<String, Map<String, BigDecimal>> summary = new TreeMap<String, Map<String, BigDecimal>>();;


    public RewardsProcessor(String json) throws JsonProcessingException {
        processData(json);

    }

    public void processData(String json) throws JsonProcessingException {
        Transaction[] transactions = new ObjectMapper().readValue(json, Transaction[].class);
        for (Transaction t : transactions){
            handleRecord(t);
        }
    }

    public Map<String, Map<String, BigDecimal>> getSummary() {
        return summary;
    }

    public void handleRecord(Transaction t)   {
        String customer = t.getName();
        Date dt = t.getDate();


        String month = PATTERN_YEAR_MONTH.format(dt);
        BigDecimal amount = t.getAmount();
        BigDecimal reward;


       Double excess =  amount.subtract(FIFTY). doubleValue() ;
        if (excess > 50d) {
            reward = FIFTY.add( new BigDecimal(2.0 *(excess -50d)));
        }
        else if (excess > 0) {
            reward = new BigDecimal(excess);
        }
        else {
            reward = BigDecimal.ZERO;
        }

        Map<String, BigDecimal> customerRewards = summary.computeIfAbsent(customer, k -> new TreeMap<>());
        BigDecimal customerRewardMonth = customerRewards.computeIfAbsent(month, k -> BigDecimal.ZERO);
        customerRewards.put(month, customerRewardMonth.add(reward));

    }

    public void printResults() {


        for (String customer : new TreeSet<String>(summary.keySet())) {
            System.out.println(String.format("\n Customer Name: %s", customer));
            BigDecimal totalRewards = BigDecimal.ZERO;
            for (String month : summary.get(customer).keySet()) {
                BigDecimal reward = summary.get(customer).computeIfAbsent(month , k-> BigDecimal.ZERO);
                totalRewards = totalRewards.add(reward);
                System.out.println(
                        String.format("\t During: %s, Rewards earned= %s points", month,   reward  ));
            }
            System.out.println(String.format("\tTotal Rewards earned: %s points", totalRewards));

        }
        
    }
}
