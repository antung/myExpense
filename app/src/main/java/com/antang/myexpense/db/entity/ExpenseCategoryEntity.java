package com.antang.myexpense.db.entity;

import com.antang.myexpense.model.ExpenseCategory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "expense_categories",
        primaryKeys = {"expenseCategoryId", "expenseCategoryName"},
        indices = {@Index(value = "expenseCategoryId", unique = true),
                @Index(value = "expenseCategoryName", unique = true)})
public class ExpenseCategoryEntity implements ExpenseCategory {
    private int expenseCategoryId;
    private int parentExpenseCategoryId;
    @NonNull
    private String expenseCategoryName;

    @Override
    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    @Override
    public int getParentExpenseCategoryId() {
        return parentExpenseCategoryId;
    }

    public void setParentExpenseCategoryId(int parentExpenseCategoryId) {
        this.parentExpenseCategoryId = parentExpenseCategoryId;
    }

    @Override
    public String getExpenseCategoryName() {
        return expenseCategoryName;
    }

    public void setExpenseCategoryName(String expenseCategoryName) {
        this.expenseCategoryName = expenseCategoryName;
    }
}
