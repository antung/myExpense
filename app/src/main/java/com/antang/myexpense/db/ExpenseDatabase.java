package com.antang.myexpense.db;

import android.content.Context;
import android.util.Log;

import com.antang.myexpense.AppExecutors;
import com.antang.myexpense.Config;
import com.antang.myexpense.db.converter.Converters;
import com.antang.myexpense.db.dao.AccountCategoryDao;
import com.antang.myexpense.db.dao.AccountDao;
import com.antang.myexpense.db.dao.ExpenseCategoryDao;
import com.antang.myexpense.db.dao.ExpenseDao;
import com.antang.myexpense.db.dao.ExpenseDetailDao;
import com.antang.myexpense.db.dao.ExpenseTagDao;
import com.antang.myexpense.db.dao.TagDao;
import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.db.entity.AccountCategoryEntity;
import com.antang.myexpense.db.entity.AccountEntity;
import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
import com.antang.myexpense.db.entity.ExpenseEntity;
import com.antang.myexpense.db.entity.ExpenseTagEntity;
import com.antang.myexpense.db.entity.TagEntity;
//import com.antang.myexpense.db.entity.AccountEntity;
//import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
//import com.antang.myexpense.db.entity.ExpenseEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {AccountCategoryEntity.class, AccountEntity.class, ExpenseCategoryEntity.class,
        ExpenseEntity.class, ExpenseTagEntity.class, TagEntity.class}, views = {ExpenseDetailView.class},
        version = 1)
@TypeConverters({Converters.class})
public abstract class ExpenseDatabase extends RoomDatabase {
    private static final String TAG = Config.TAG_PREFIX + "ExpenseDatabase";
    private static final boolean DEBUG = Config.DEBUG;
    @VisibleForTesting
    public static final String DATABASE_NAME = "expense-db";
    private static ExpenseDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public abstract AccountCategoryDao accountCategoryDao();
    public abstract AccountDao accountDao();
    public abstract ExpenseCategoryDao expenseCategoryDao();
    public abstract ExpenseDao expenseDao();
    public abstract ExpenseDetailDao expenseDetailDao();
    public abstract ExpenseTagDao expenseTagDao();
    public abstract TagDao tagDao();

    private AppExecutors mAppExecutors;

    public static ExpenseDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (ExpenseDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context, executors);
                    sInstance.setAppExecutors(executors).
                            updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    private static ExpenseDatabase buildDatabase(final Context context, final AppExecutors executors) {
        return Room.databaseBuilder(context.getApplicationContext(), ExpenseDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
//                        if (DEBUG) {
//                            Log.d(TAG, "buildDatabase: onOpen()", (new Exception()).fillInStackTrace());
//                        }
                    }

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        if (DEBUG) {
//                            Log.d(TAG, "buildDatabase: onCreate()", (new Exception()).fillInStackTrace());
//                        }
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            ExpenseDatabase database = ExpenseDatabase.getInstance(context, executors);
                            List<AccountCategoryEntity> accountCategoryEntities =
                                    DefaultDataGenerator.generateDefaultAccountCategories();
                            List<ExpenseCategoryEntity> expenseCategoryEntities =
                                    DefaultDataGenerator.generateDefaultExpenseCategoryEntities();
                            List<TagEntity> tagEntities =
                                    DefaultDataGenerator.generateDefaultTags();
                            if (DEBUG) {
                                Log.d(TAG, "buildDatabase: insert default data");
                            }
                            database.insertAccountCategory(
                                    accountCategoryEntities,
                                    expenseCategoryEntities,
                                    tagEntities);

                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                })
                .build();
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    public void insertAccount(AccountEntity accountEntity) {
        mAppExecutors.diskIO().execute(() -> {
            accountDao().insert(accountEntity);
        });
    }

    public void insertAccountCategory(AccountCategoryEntity accountCategoryEntity) {
        mAppExecutors.diskIO().execute(() -> {
            accountCategoryDao().insert(accountCategoryEntity);
        });
    }

    public void insertExpenseCategory(ExpenseCategoryEntity expenseCategoryEntity) {
        mAppExecutors.diskIO().execute(() -> {
            expenseCategoryDao().insert(expenseCategoryEntity);
        });
    }

    public void insertTag(TagEntity tagEntity) {
        mAppExecutors.diskIO().execute(() -> {
            tagDao().insert(tagEntity);
        });
    }

    public void insertExpense(ExpenseEntity expenseEntity, List<ExpenseTagEntity> expenseTagEntityList) {
        mAppExecutors.diskIO().execute(() -> {
            runInTransaction(() -> {
                expenseDao().insert(expenseEntity);
                if (expenseTagEntityList != null) {
                    expenseTagDao().insertAll(expenseTagEntityList);
                }
            });
        });
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private ExpenseDatabase setAppExecutors(AppExecutors executors) {
        mAppExecutors = executors;
        return this;
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    private void insertAccountCategory(final List<AccountCategoryEntity> accountCategoryEntities,
                                              final List<ExpenseCategoryEntity> expenseCategoryEntities,
                                              final List<TagEntity> tagEntities) {
        runInTransaction(() -> {
            accountCategoryDao().insertAll(accountCategoryEntities);
            expenseCategoryDao().insertAll(expenseCategoryEntities);
            tagDao().insertAll(tagEntities);
        });
    }
}
