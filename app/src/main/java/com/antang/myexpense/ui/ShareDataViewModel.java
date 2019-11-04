package com.antang.myexpense.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * this intent to be share data across Fragments, so the owner should be Activity rather than
 * Fragment
 * */
public class ShareDataViewModel extends ViewModel {
    private final MutableLiveData<DataEntry> mDataEntry = new MutableLiveData<DataEntry>();
    public void setDataEntry(DataEntry entry) {
        mDataEntry.setValue(entry);
    }

    public LiveData<DataEntry> getDataEntry() {
        return mDataEntry;
    }
}
