package com.antang.myexpense.model;

public interface Account {
    int getAccountId();
    int getParentAccountId();
    String getOwnerName();
    int getAccountCategoryId();
    String getAccountNo();
    String getCardNo();
    String getPasscode();
    String getAccountDisplayName(); // eg. BOA-Andy-1044
}
