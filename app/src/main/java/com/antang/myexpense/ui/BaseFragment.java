package com.antang.myexpense.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antang.myexpense.DataRepository;
import com.antang.myexpense.MyExpenseApplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment extends Fragment {
    protected ViewModelProvider mViewModelProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModelProvider = new ViewModelProvider(this);
    }

    public void onConfigActionButton(TextView actionButton) {
        actionButton.setVisibility(View.VISIBLE);
    }

    public void onConfigLogoButton(ImageView logoButton) {
        logoButton.setVisibility(View.VISIBLE);
    }

    public void onActionButtonClicked() {
        if (handleActionButtonClicked()) {
            finishSelf();
        }
    }

    public boolean onLogoButtonClicked() {
        finishSelf();
        return true;
    }

    protected boolean handleActionButtonClicked() {
        return false;
    }

    protected void finishSelf() {
        getParentFragmentManager().popBackStackImmediate();
    }

    protected DataRepository getDataRepository() {
        return ((MyExpenseApplication)getActivity().getApplication()).getRepository();
    }

    protected ShareDataViewModel getShareDataViewModel() {
        return (new ViewModelProvider(getActivity())).get(ShareDataViewModel.class);
    }
}