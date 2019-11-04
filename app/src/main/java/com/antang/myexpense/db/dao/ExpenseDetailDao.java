/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antang.myexpense.db.dao;

import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.db.databaseview.ExpenseStatistics;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ExpenseDetailDao {
    @Query("SELECT * FROM expense_detail_view")
    LiveData<List<ExpenseDetailView>> getAllExpenses();

    @Query("SELECT * FROM expense_detail_view WHERE dateTime BETWEEN  :from AND :to")
    LiveData<List<ExpenseDetailView>> getAllExpensesBetweenDates(Date from, Date to);

    @Query("SELECT * FROM expense_detail_view WHERE dateTime >  :from")
    LiveData<List<ExpenseDetailView>> getAllExpensesFromDate(Date from);

    @Query("SELECT * FROM expense_detail_view WHERE dateTime < :to")
    LiveData<List<ExpenseDetailView>> getAllExpensesToDate(Date to);

    @Query("SELECT * FROM expense_detail_view WHERE strftime('%Y', dateTime) like :year AND strftime('%m', dateTime) like :month")
    LiveData<List<ExpenseDetailView>> getAllExpensesByYearAndMonth(int year, int month);

    @Query("SELECT * FROM expense_detail_view WHERE  expenseId = :expenseId ORDER BY dateTime ASC")
    LiveData<ExpenseDetailView> getAllExpensesByExpenseId(long expenseId);

    @Query("SELECT * FROM expense_detail_view WHERE  accountId = :accountId ORDER BY dateTime ASC")
    LiveData<ExpenseDetailView> getAllExpensesByAccountId(long accountId);

    @Query("SELECT * FROM expense_detail_view WHERE expenseType = :expenseType ORDER BY dateTime ASC")
    LiveData<ExpenseDetailView> getAllExpensesByType(byte expenseType);

    @Query("SELECT SUM(amount) as total FROM expense_detail_view WHERE expenseType = :expenseType")
    LiveData<ExpenseStatistics> getExpenseSumByType(byte expenseType);

    @Query("SELECT SUM(amount) as total FROM expense_detail_view WHERE expenseType = :expenseType AND strftime('%Y', dateTime) like :year AND strftime('%m', dateTime) like :month")
    LiveData<ExpenseStatistics> getExpenseSumByTypeAndyYearMonth(byte expenseType, int year, int month);

    @Query("SELECT SUM(amount) as total FROM expense_detail_view WHERE expenseType = :expenseType AND strftime('%Y', dateTime) like :year")
    LiveData<ExpenseStatistics> getExpenseSumByTypeAndyYear(byte expenseType, int year);
}
