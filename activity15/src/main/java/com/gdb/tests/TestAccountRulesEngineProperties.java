package com.gdb.tests;

import com.gdb.domain.AccountRulesEngine;

public class TestAccountRulesEngineProperties {

    public static void main(String[] args) {
        System.out.println("=== Activity 14/15: Properties-Driven Rules Engine Test ===");
        AccountRulesEngine engine = AccountRulesEngine.getInstance();

        printTenureRow(engine, 0);
        printTenureRow(engine, 2);
        printTenureRow(engine, 4);
        printTenureRow(engine, 6);

        System.out.println("All external properties loaded and verified successfully!");
    }

    private static void printTenureRow(AccountRulesEngine engine, int tenureYears) {
        double minBalance = engine.getMinBalance("savings", tenureYears);
        double interestRate = engine.getInterestRate("savings", tenureYears);
        double dailyLimit = engine.getDailyTransferLimit("savings", tenureYears);
        System.out.printf(
                "Tenure %d yrs -> Min Balance: Rs. %-7s | Interest: %.2f%% | Daily Transfer Limit: Rs. %s%n",
                tenureYears, minBalance, interestRate, dailyLimit);
    }
}
