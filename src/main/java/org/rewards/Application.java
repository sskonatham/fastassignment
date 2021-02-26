/* Copyright 2019 Mentor Graphics Corporation
 *            All Rights Reserved
 *
 * THIS WORK CONTAINS TRADE SECRET
 * AND PROPRIETARY INFORMATION WHICH IS THE
 * PROPERTY OF MENTOR GRAPHICS
 * CORPORATION OR ITS LICENSORS AND IS
 * SUBJECT TO LICENSE TERMS.
 */
package org.rewards;

import org.rewards.service.RewardsProcessor;

import java.io.InputStream;
import java.util.Scanner;


public class Application {

    public static void main(String[] args) {

        try {
            String jsonInput = loadInputData("input.json");
            RewardsProcessor etl = new RewardsProcessor(jsonInput);
            etl.printResults();
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": Unable to load file ");
            e.printStackTrace();
        }
    }

    private static String loadInputData(String f) {
        ClassLoader cl = Application.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(f);
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String json = s.hasNext() ? s.next() : "";
        return json;
    }


}
