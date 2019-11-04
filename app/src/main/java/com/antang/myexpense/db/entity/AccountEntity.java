package com.antang.myexpense.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.antang.myexpense.model.Account;

@Entity(tableName = "accounts", primaryKeys = {"accountId", "accountDisplayName"}, foreignKeys = {
        @ForeignKey(entity = AccountCategoryEntity.class,
                parentColumns = "accountCategoryId",
                childColumns = "accountCategoryId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "accountId", unique = true),
                @Index(value = "accountDisplayName", unique = true),
                @Index(value = "accountCategoryId")})
public class AccountEntity implements Account {
    private int accountId;
    private int parentAccountId;
    private String ownerName;
    private int accountCategoryId;
    private String accountNo;
    private String cardNo;
    private String passcode;

    @NonNull
    private String accountDisplayName;


    @Override
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(int parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public int getAccountCategoryId() {
        return accountCategoryId;
    }

    public void setAccountCategoryId(int accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    @Override
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Override
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    @Override
    public String getAccountDisplayName() {
        return accountDisplayName;
    }

    public void setAccountDisplayName(String accountDisplayName) {
        this.accountDisplayName = accountDisplayName;
    }
}
