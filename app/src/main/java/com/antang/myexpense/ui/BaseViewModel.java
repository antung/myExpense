package com.antang.myexpense.ui;

import android.app.Application;

import com.antang.myexpense.DataRepository;
import com.antang.myexpense.MyExpenseApplication;

import androidx.lifecycle.AndroidViewModel;

public class BaseViewModel extends AndroidViewModel {
    protected DataRepository mDataRepository;

    public BaseViewModel(Application application) {
        super(application);
        mDataRepository = ((MyExpenseApplication) application).getRepository();
    }
}
