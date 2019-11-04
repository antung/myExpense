package com.antang.myexpense.model;

public interface ExpenseCategory {
    int getExpenseCategoryId();
    int getParentExpenseCategoryId();
    String getExpenseCategoryName();
}
