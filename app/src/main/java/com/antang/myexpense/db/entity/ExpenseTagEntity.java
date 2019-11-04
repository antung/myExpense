package com.antang.myexpense.db.entity;

import com.antang.myexpense.model.ExpenseTags;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

/**
 * Expense & Tags (Many-to-Many relationship)
 */
@Entity(tableName = "expense_tags", primaryKeys = {"expenseId", "tagId"},
        foreignKeys = {
                @ForeignKey(entity = ExpenseEntity.class,
                        parentColumns = "expenseId",
                        childColumns = "expenseId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = TagEntity.class,
                        parentColumns = "tagId",
                        childColumns = "tagId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "tagId"),
                @Index(value = "expenseId")})
public class ExpenseTagEntity implements ExpenseTags {
    private long expenseId;
    private int tagId;

    @Ignore
    public String tagName;

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }


    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public int getTagId() {
        return tagId;
    }

    @Override
    public long getExpenseId() {
        return expenseId;
    }
}
