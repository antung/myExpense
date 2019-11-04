package com.antang.myexpense.model;

import java.util.Date;

/**
 * tracks income and outcome records in fact
 * */

public interface Expense {
    long getExpenseId();
    Date getDateTime();
    byte getExpenseType(); // 0-outcome, 1-income
    int getAccountId();
    int getExpenseCategoryId();
    int getAmount(); // for refund, it should outcome but amount is negative
    long getParentId(); // eg. reference expense id if it's a refund record
    String getChildIds();
    String getSnapshotPath(); // snapshot picture path
}
