package com.antang.myexpense.ui.view;

import android.app.Application;

import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.ui.BaseViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ViewExpenseViewModel extends BaseViewModel {

    public ViewExpenseViewModel(Application application) {
        super(application);
    }

    public LiveData<List<ExpenseDetailView>> getAllDetailedExpenses() {
        return mDataRepository.getAllDetailedExpenses(null, null);
    }
}