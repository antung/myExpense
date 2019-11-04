package com.antang.myexpense;

import android.util.Log;

import com.antang.myexpense.db.ExpenseDatabase;
import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.db.databaseview.ExpenseStatistics;
import com.antang.myexpense.db.entity.AccountCategoryEntity;
import com.antang.myexpense.db.entity.AccountEntity;
import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
import com.antang.myexpense.db.entity.ExpenseEntity;
import com.antang.myexpense.db.entity.ExpenseTagEntity;
import com.antang.myexpense.db.entity.TagEntity;
import com.antang.myexpense.model.AccountCategory;
import com.antang.myexpense.model.TotalExpenseInfo;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class DataRepository {
    private static final String TAG = Config.TAG_PREFIX + "DataRepository";
    private static final boolean DEBUG = Config.DEBUG;
    private static DataRepository sInstance;

    private final ExpenseDatabase mDatabase;
    private MediatorLiveData<List<AccountEntity>> mObservableAccount;

    private DataRepository(final ExpenseDatabase database) {
        mDatabase = database;
        mObservableAccount = new MediatorLiveData<>();
        // because adding Account will be handled with different UI fragment, so
        // we need to observe it
        mObservableAccount.addSource(mDatabase.accountDao().getAccounts(),
                (accountEntities) -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        if (DEBUG) {
                            Log.d(TAG, "AccountEntities onChanged() " + accountEntities);
                        }
                        mObservableAccount.postValue(accountEntities);
                    }
                });
    }

    public static DataRepository getInstance(final ExpenseDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<AccountCategoryEntity>> getAccountCategories() {
        return mDatabase.accountCategoryDao().getAccountCategories();
    }

    public void insertAccountCategory(AccountCategoryEntity accountCategoryEntity) {
        mDatabase.insertAccountCategory(accountCategoryEntity);
    }

    public LiveData<List<ExpenseCategoryEntity>> getExpenseCategories() {
        return mDatabase.expenseCategoryDao().getExpenseCategories();
    }

    public void insertExpenseCategory(ExpenseCategoryEntity expenseCategoryEntity) {
        mDatabase.insertExpenseCategory(expenseCategoryEntity);
    }

    public LiveData<List<TagEntity>> getAllTags() {
        return mDatabase.tagDao().getAllTags();
    }

    public void insertTag(TagEntity tagEntity) {
        mDatabase.insertTag(tagEntity);
    }

    public LiveData<List<AccountEntity>> getAccounts() {
        return mDatabase.accountDao().getAccounts();
    }

    public void insertAccount(AccountEntity accountEntity) {
        mDatabase.insertAccount(accountEntity);
    }

    public void insertExpense(ExpenseEntity expenseEntity, List<ExpenseTagEntity> expenseTagEntityList) {
        mDatabase.insertExpense(expenseEntity, expenseTagEntityList);
    }

    public LiveData<List<ExpenseDetailView>> getAllDetailedExpenses(Date from, Date to) {
        if (from != null && to != null) {
            return mDatabase.expenseDetailDao().getAllExpensesBetweenDates(from, to);
        } else if (from != null) {
            return mDatabase.expenseDetailDao().getAllExpensesFromDate(from);
        } else if (to != null) {
            return mDatabase.expenseDetailDao().getAllExpensesToDate(to);
        } else {
            return mDatabase.expenseDetailDao().getAllExpenses();
        }
    }

    public LiveData<ExpenseStatistics> getExpenseSum(byte expenseType, int year){
        if (year > 0) {
            return mDatabase.expenseDetailDao().getExpenseSumByTypeAndyYear(expenseType, year);
        } else {
            return mDatabase.expenseDetailDao().getExpenseSumByType(expenseType);
        }
    }
}
