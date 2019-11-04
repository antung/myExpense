package com.antang.myexpense.ui.edit;

import android.app.Application;

import com.antang.myexpense.db.entity.AccountCategoryEntity;
import com.antang.myexpense.db.entity.AccountEntity;
import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
import com.antang.myexpense.db.entity.ExpenseEntity;
import com.antang.myexpense.db.entity.ExpenseTagEntity;
import com.antang.myexpense.db.entity.TagEntity;
import com.antang.myexpense.ui.BaseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditViewModel extends BaseViewModel {

    private MutableLiveData<String> mText;

    public EditViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void addNewAccountCategory(int id, String label) {
        AccountCategoryEntity entity = new AccountCategoryEntity();
        entity.setAccountCategoryId(id);
        entity.setAccountCategoryName(label);
        mDataRepository.insertAccountCategory(entity);
    }

    public void addNewAccount(int id, String ownerName, String accountNo,
                                    String accountDisplayName, int accountCategoryId) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountId(id);
        entity.setOwnerName(ownerName);
        entity.setAccountNo(accountNo);
        entity.setAccountDisplayName(accountDisplayName);
        entity.setAccountCategoryId(accountCategoryId);
        mDataRepository.insertAccount(entity);
    }

    public LiveData<List<AccountCategoryEntity>> getAccountCategories() {
        return mDataRepository.getAccountCategories();
    }

    public LiveData<List<AccountEntity>> getAccounts() {
        return mDataRepository.getAccounts();
    }

    public LiveData<List<ExpenseCategoryEntity>> getExpenseCategories() {
        return mDataRepository.getExpenseCategories();
    }

    public void addNewExpenseCategory(int id, String label) {
        ExpenseCategoryEntity entity = new ExpenseCategoryEntity();
        entity.setExpenseCategoryId(id);
        entity.setExpenseCategoryName(label);
        mDataRepository.insertExpenseCategory(entity);
    }

    public LiveData<List<TagEntity>> getAllTags() {
        return mDataRepository.getAllTags();
    }

    public void addNewTag(int id, String label) {
        TagEntity entity = new TagEntity();
        entity.setTagId(id);
        entity.setTagName(label);
        mDataRepository.insertTag(entity);
    }

    public void addNewExpense(long id, byte expenseType,
                              Date datetime, int accountId,
                              int expenseCategoryId, int amount,
                              String snapshot, int[] tagIds) {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setExpenseId(id);
        expenseEntity.setExpenseType(expenseType);
        expenseEntity.setDateTime(datetime);
        expenseEntity.setAccountId(accountId);
        expenseEntity.setAmount(amount);
        expenseEntity.setExpenseCategoryId(expenseCategoryId);
        List<ExpenseTagEntity> expenseTagEntityList = null;
        if (tagIds != null && tagIds.length > 0) {
            expenseTagEntityList = new ArrayList<>(tagIds.length);
            for (int tagId : tagIds) {
                ExpenseTagEntity expenseTagEntity = new ExpenseTagEntity();
                expenseTagEntity.setExpenseId(id);
                expenseTagEntity.setTagId(tagId);
                expenseTagEntityList.add(expenseTagEntity);
            }
        }

        mDataRepository.insertExpense(expenseEntity, expenseTagEntityList);
    }
}