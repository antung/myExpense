package com.antang.myexpense.ui.home;

import android.app.Application;
import android.util.Log;

import com.antang.myexpense.Config;
import com.antang.myexpense.MyExpenseApplication;
import com.antang.myexpense.R;
import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.db.databaseview.ExpenseStatistics;
import com.antang.myexpense.model.TotalExpenseInfo;
import com.antang.myexpense.ui.BaseViewModel;
import com.antang.myexpense.utils.Const;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends BaseViewModel {
    private static final String TAG = Config.TAG_PREFIX + "HomeViewModel";
    private static final boolean DEBUG = Config.DEBUG;

    private MutableLiveData<TotalExpenseInfo> mTotalExpenseData = new MutableLiveData<>();
    private TotalExpenseInfo mTotalExpenseInfo = new TotalExpenseInfo();

    public HomeViewModel(Application application) {
        super(application);
    }

    public LiveData<TotalExpenseInfo> getExpenseStatistics(LifecycleOwner owner, int year) {
        mTotalExpenseInfo.data.clear();
        final LiveData<ExpenseStatistics> sumOfIncomeLiveData = mDataRepository.getExpenseSum(Const.EXPENSE_TYPE_INCOME, year);
        sumOfIncomeLiveData.observe(owner, (ExpenseStatistics statistics) -> {
            if (DEBUG) {
                Log.d(TAG, "sumOfIncomeLiveData cb:" + statistics.total);
            }
            mTotalExpenseInfo.data.addChild(Const.TYPE_INCOME,
                    getApplication().getString(R.string.income), statistics.total);
            // let's hold to notify UI to update until sumOfOutcome ready
        });
        final LiveData<ExpenseStatistics> sumOfOutcomeLiveData = mDataRepository.getExpenseSum(Const.EXPENSE_TYPE_OUTCOME, year);
        sumOfOutcomeLiveData.observe(owner, (ExpenseStatistics statistics) -> {
            if (DEBUG) {
                Log.d(TAG, "sumOfOutcomeLiveData cb:" + statistics.total);
            }
            mTotalExpenseInfo.data.addChild(Const.TYPE_OUTCOME,
                    getApplication().getString(R.string.outcome), statistics.total);
            mTotalExpenseData.postValue(mTotalExpenseInfo);
        });
        return mTotalExpenseData;
    }
}