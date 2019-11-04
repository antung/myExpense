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

import com.antang.myexpense.db.entity.AccountCategoryEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AccountCategoryDao {
    @Query("SELECT * FROM account_categories")
    LiveData<List<AccountCategoryEntity>> getAccountCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AccountCategoryEntity> accountCategoryEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountCategoryEntity accountCategoryEntity);

    @Query("SELECT * FROM account_categories WHERE accountCategoryId = :accountCategoryId")
    LiveData<AccountCategoryEntity> getAccountCategory(int accountCategoryId);
}
