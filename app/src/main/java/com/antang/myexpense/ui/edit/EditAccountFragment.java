package com.antang.myexpense.ui.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.antang.myexpense.Config;
import com.antang.myexpense.R;
import com.antang.myexpense.db.entity.AccountCategoryEntity;
import com.antang.myexpense.ui.BaseFragment;
import com.antang.myexpense.ui.DataEntry;
import com.antang.myexpense.ui.widgets.SimpleTextGridView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class EditAccountFragment extends BaseFragment {
    static final String TAG = Config.TAG_PREFIX + "EditAccountFragment";
    private static final boolean DEBUG = Config.DEBUG;

    private EditText mEdtOwner;
    private EditText mEdtAccountNo;
    private EditText mEdtDisplayName;
    private SimpleTextGridView mGvAccountCategories;
    private EditViewModel mEditViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditViewModel = mViewModelProvider.get(EditViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_account, container, false);
        mEdtAccountNo = root.findViewById(R.id.tv_account_no);
        mEdtOwner = root.findViewById(R.id.tv_owner);
        mGvAccountCategories = root.findViewById(R.id.gv_account_categories);
        mEdtDisplayName = root.findViewById(R.id.tv_account_display_name);
        init();
        return root;
    }

    @Override
    public boolean handleActionButtonClicked() {
        return handleSaveAccount();
    }

    private void init() {
        mGvAccountCategories.setListener(new SimpleTextGridView.SimpleTextGridViewListener() {
            @Override
            public void onNewEntryAdded(int id, String label) {
                if (DEBUG) {
                    Log.d(TAG, "onNewEntryAdded: Account category " + id + ", label=" + label);
                }
                mEditViewModel.addNewAccountCategory(id, label);
            }
        });
        final LiveData<List<AccountCategoryEntity>> accountCategories = mEditViewModel.getAccountCategories();
        accountCategories.observe(this,
                (List<AccountCategoryEntity> accountCategoryEntities) -> {
                    if (DEBUG) {
                        Log.d(TAG, "Account categories LiveData cb:" + accountCategoryEntities);
                    }
                    if (accountCategoryEntities != null && !accountCategoryEntities.isEmpty()) {
                        SparseArray<String> data = new SparseArray<>(accountCategoryEntities.size());
                        for (AccountCategoryEntity entity : accountCategoryEntities) {
                            data.append(entity.getAccountCategoryId(), entity.getAccountCategoryName());
                        }
                        mGvAccountCategories.setData(data);
                    }

                    // we don't want it update automatically after we add new category
                    accountCategories.removeObservers(this);
                });

        mEdtOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAccountDisplayName();
            }
        });

        mEdtAccountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAccountDisplayName();
            }
        });
    }

    private void updateAccountDisplayName() {
        CharSequence ownerName = mEdtOwner.getText();
        CharSequence accountNo = mEdtAccountNo.getText();
        if (!TextUtils.isEmpty(ownerName) && !TextUtils.isEmpty(accountNo)) {
            int len = accountNo.length();
            CharSequence subAccountNo = accountNo.subSequence(len > 4 ? len - 4 : 0, len);
            String displayName = getString(R.string.account_display_name_format,
                    ownerName.toString(),
                    subAccountNo.toString());
            mEdtDisplayName.setText(displayName);
        }
    }

    private boolean handleSaveAccount() {
        // TO-DO: conflict handling

        CharSequence displayName = mEdtDisplayName.getText();
        CharSequence ownerName = mEdtOwner.getText();
        CharSequence accountNo = mEdtAccountNo.getText();
        if (TextUtils.isEmpty(displayName)) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.text_account_display_name)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(ownerName)) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.text_owner)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(accountNo)) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.text_account_no)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        List<DataEntry> categories = mGvAccountCategories.getSelectedData();
        if (categories == null || categories.isEmpty()) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.category)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        final int id = displayName.toString().hashCode();
        DataEntry category = categories.get(0);
        String accountDisplayName = displayName.toString();
        mEditViewModel.addNewAccount(id, ownerName.toString(),
                accountNo.toString(), accountDisplayName, category.id);
        DataEntry newEntry = new DataEntry(id, accountDisplayName);
        getShareDataViewModel().setDataEntry(newEntry);

        return true;
    }
}