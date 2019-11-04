package com.antang.myexpense.db.entity;

import com.antang.myexpense.model.AccountCategory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

//@Entity(tableName = "account_categories", primaryKeys = {"accountCategoryId", "accountCategoryName"},
//        indices = {@Index(value = {"accountCategoryId", "accountCategoryName"}, unique = true)})
// why? error: com.antang.myexpense.db.entity.AccountEntity has a foreign key (accountCategoryId) that references com.antang.myexpense.db.entity.AccountCategoryEntity (accountCategoryId) but com.antang.myexpense.db.entity.AccountCategoryEntity does not have a unique index on those columns nor the columns are its primary key.

@Entity(tableName = "account_categories", primaryKeys = {"accountCategoryId", "accountCategoryName"},
        indices = {@Index(value = "accountCategoryId", unique = true),
                @Index(value = "accountCategoryName", unique = true)})
public class AccountCategoryEntity implements AccountCategory {
    private int accountCategoryId;
    @NonNull
    private String accountCategoryName;

    @Override
    public int getAccountCategoryId() {
        return accountCategoryId;
    }

    public void setAccountCategoryId(int accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    @Override
    public String getAccountCategoryName() {
        return accountCategoryName;
    }

    public void setAccountCategoryName(String accountCategoryName) {
        this.accountCategoryName = accountCategoryName;
    }
}
