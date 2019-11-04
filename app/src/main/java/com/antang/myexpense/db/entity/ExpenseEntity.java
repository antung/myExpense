package com.antang.myexpense.db.entity;

import com.antang.myexpense.model.Expense;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "expenses", foreignKeys = {
        @ForeignKey(entity = AccountEntity.class,
                parentColumns = "accountId",
                childColumns = "accountId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = ExpenseCategoryEntity.class,
                parentColumns = "expenseCategoryId",
                childColumns = "expenseCategoryId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "expenseId", unique = true),
                @Index(value = {"accountId"}),
                @Index(value = "expenseCategoryId"),
                @Index(value = "dateTime"),
                @Index(value = "expenseType")})
public class ExpenseEntity implements Expense {
    @PrimaryKey
    private long expenseId; // this suppose be the primary key, but Specifying a primary key for an FTS-table-backed entity is optional
    private byte expenseType; // 1-outcome, 2-income, 0-N/A
    private Date dateTime;
    private int accountId;
    private int expenseCategoryId;
    private int amount; // integer, it doens't make sense to track exactly, otherwise it has to be float type
    private long parentId; // reference, eg. expense-2 is refund of expense-1, then id of expense-1 is the parent id
    private String childIds; // reference
    private String snapshotPath;

    @Override
    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public byte getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(byte expenseType) {
        this.expenseType = expenseType;
    }

    @Override
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds;
    }

    @Override
    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }
}
