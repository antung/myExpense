package com.antang.myexpense.db.databaseview;

import androidx.room.DatabaseView;

@DatabaseView(viewName = "expense_detail_view",
        value = "SELECT expenses.expenseId, expenses.expenseType, expenses.dateTime, " +
                "expenses.accountId, accounts.accountDisplayName, expenses.expenseCategoryId, " +
                "expense_categories.expenseCategoryName, expenses.amount, expenses.parentId, " +
                "expenses.childIds, expenses.snapshotPath  FROM expenses " +
                "INNER JOIN accounts ON expenses.accountId = accounts.accountId " +
                "INNER JOIN expense_categories ON expenses.expenseCategoryId = expense_categories.expenseCategoryId " +
                "ORDER BY expenses.dateTime DESC")
public class ExpenseDetailView {
    public long expenseId; // this suppose be the primary key, but Specifying a primary key for an FTS-table-backed entity is optional
    public byte expenseType; // 0-outcome, 1-income
    public long dateTime; // epoch time
    public int accountId;
    public String accountDisplayName;
    public int expenseCategoryId;
    public String expenseCategoryName;
    public int amount; // integer, it doens't make sense to track exactly, otherwise it has to be float type
    public long parentId; // reference, eg. expense-2 is refund of expense-1, then id of expense-1 is the parent id
    public String childIds; // reference
    public String snapshotPath;
}
