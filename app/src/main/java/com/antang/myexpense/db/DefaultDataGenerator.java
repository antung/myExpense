package com.antang.myexpense.db;

import com.antang.myexpense.db.entity.AccountCategoryEntity;
import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
import com.antang.myexpense.db.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;

public class DefaultDataGenerator {
    private static final String[] DEF_ACCOUNT_CATEGORIES = new String[] {
            "Checking", "Saving", "Credit Card", "Cash"
    };

    private static final String[] DEF_EXPENSE_TAGS = new String[] {
            "Costco", "Target", "Walmart", "McDonalds"
    };

    private static final String[] DEF_EXPENSE_CATEGORIES = new String[] {
            "Salary", "Bonus", "Refund", "Home Rent", "Gasoline", "Utility", "Phone bill",
            "Internet", "Grocery", "Auto Insurance", "Education", "Health&Wellness", "Gift&Donation"
    };

    public static List<AccountCategoryEntity> generateDefaultAccountCategories() {
        List<AccountCategoryEntity> entities = new ArrayList<>(DEF_ACCOUNT_CATEGORIES.length);
        for (String category : DEF_ACCOUNT_CATEGORIES) {
            AccountCategoryEntity entity = new AccountCategoryEntity();
            entity.setAccountCategoryId(category.hashCode());
            entity.setAccountCategoryName(category);
            entities.add(entity);
        }
        return entities;
    }

    public static List<ExpenseCategoryEntity> generateDefaultExpenseCategoryEntities() {
        List<ExpenseCategoryEntity> entities = new ArrayList<>(DEF_EXPENSE_CATEGORIES.length);
        for (String category : DEF_EXPENSE_CATEGORIES) {
            ExpenseCategoryEntity entity = new ExpenseCategoryEntity();
            entity.setExpenseCategoryId(category.hashCode());
            entity.setExpenseCategoryName(category);
            entities.add(entity);
        }
        return entities;
    }

    public static List<TagEntity> generateDefaultTags() {
        List<TagEntity> entities = new ArrayList<>(DEF_EXPENSE_TAGS.length);
        for (String tag : DEF_EXPENSE_TAGS) {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setTagId(tag.hashCode());
            tagEntity.setTagName(tag);
            entities.add(tagEntity);
        }
        return entities;
    }
}
