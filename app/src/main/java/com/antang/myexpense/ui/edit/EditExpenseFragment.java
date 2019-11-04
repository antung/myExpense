package com.antang.myexpense.ui.edit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.antang.myexpense.Config;
import com.antang.myexpense.R;
import com.antang.myexpense.db.entity.AccountEntity;
import com.antang.myexpense.db.entity.ExpenseCategoryEntity;
import com.antang.myexpense.db.entity.TagEntity;
import com.antang.myexpense.ui.BaseFragment;
import com.antang.myexpense.ui.DataEntry;
import com.antang.myexpense.ui.widgets.SimpleTextGridView;
import com.antang.myexpense.utils.Const;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EditExpenseFragment extends BaseFragment {
    private static final String TAG = Config.TAG_PREFIX + "EditExpenseFragment";
    private static final boolean DEBUG = Config.DEBUG;

    private EditViewModel mEditViewModel;
    private Calendar mCalendar = Calendar.getInstance();
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvAmount;
    private SimpleTextGridView mGvAccounts;
    private SimpleTextGridView mGvExpenseCategories;
    private SimpleTextGridView mGvTags;
    private View mContentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditViewModel = mViewModelProvider.get(EditViewModel.class);
    }

    // onCreateView will be called when return from EditAccountFragmemt
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            View root = inflater.inflate(R.layout.fragment_edit_expense, container, false);
            mTvDate = root.findViewById(R.id.tv_date);
            updateDateTextView();
            mTvDate.setOnClickListener((final View v) -> {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.setOnDateSetListener((DatePicker view, int year, int month, int dayOfMonth) -> {
                    if (DEBUG) {
                        Log.d(TAG, "onDateSet: year=" + year + ", month=" + month + ", dayOfMonth=" + dayOfMonth);
                    }
                    mCalendar.set(year, month, dayOfMonth);
                    updateDateTextView();
                });
                dialog.show();
            });

            mTvTime = root.findViewById(R.id.tv_time);
            updateTimeTextView();
            mTvTime.setOnClickListener((final View v) -> {
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(getContext(), (TimePicker view, int hourOfDay, int minute) -> {
                    if (DEBUG) {
                        Log.d(TAG, "onTimeSet: hourOfDay=" + hourOfDay + ", min" + minute);
                    }
                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    updateTimeTextView();
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                dialog.show();
            });

            mTvAmount = root.findViewById(R.id.tv_amount);

            mGvAccounts = root.findViewById(R.id.gv_accounts);
            initAccountsView();

            mGvExpenseCategories = root.findViewById(R.id.gv_expense_categories);
            initExpenseCategoriesView();

            mGvTags = root.findViewById(R.id.gv_expense_tags);
            initTagsView();
            mContentView = root;
        }

        return mContentView;
    }

    @Override
    public boolean handleActionButtonClicked() {
        return handleSaveExpense();
    }

    private void initAccountsView() {
        mGvAccounts.setListener(new SimpleTextGridView.SimpleTextGridViewListener() {
            @Override
            public boolean onInterceptAddNewAction() {
                launchAddAccountFragment();
                return true;
            }
        });

        final LiveData<List<AccountEntity>> accounts = mEditViewModel.getAccounts();
        accounts.observe(this, (List<AccountEntity> accountList) -> {
            if (DEBUG) {
                Log.d(TAG, "Accounts live data cb: " + accountList);
            }
            SparseArray<String> data = new SparseArray<>(); // we'd show "Add new" at least
            if (accountList != null && !accountList.isEmpty()) {
                for (AccountEntity entity : accountList) {
                    data.append(entity.getAccountId(), entity.getAccountDisplayName());
                }
            }
            mGvAccounts.setData(data);
            accounts.removeObservers(this);
        });
    }

    private void initExpenseCategoriesView() {
        mGvExpenseCategories.setListener(new SimpleTextGridView.SimpleTextGridViewListener() {
            @Override
            public void onNewEntryAdded(int id, String label) {
                if (DEBUG) {
                    Log.d(TAG, "onNewEntryAdded:Expense category " + id + ", label=" + label);
                }
                mEditViewModel.addNewExpenseCategory(id, label);
            }
        });

        final LiveData<List<ExpenseCategoryEntity>> expenseCategories = mEditViewModel.getExpenseCategories();
        expenseCategories.observe(this,
                (List<ExpenseCategoryEntity> expenseCategoriesEntities) -> {
                    if (DEBUG) {
                        Log.d(TAG, "Expense categories LiveData cb:" + expenseCategoriesEntities);
                    }
                    if (expenseCategoriesEntities != null && !expenseCategoriesEntities.isEmpty()) {
                        SparseArray<String> data = new SparseArray<>(expenseCategoriesEntities.size());
                        for (ExpenseCategoryEntity entity : expenseCategoriesEntities) {
                            data.append(entity.getExpenseCategoryId(), entity.getExpenseCategoryName());
                        }
                        mGvExpenseCategories.setData(data);
                    }

                    // we don't want it update automatically after we add new category
                    expenseCategories.removeObservers(this);
                });
    }

    private void initTagsView() {
        mGvTags.setListener(new SimpleTextGridView.SimpleTextGridViewListener() {
            @Override
            public void onNewEntryAdded(int id, String label) {
                if (DEBUG) {
                    Log.d(TAG, "onNewEntryAdded: Tag " + id + ", label=" + label);
                }
                mEditViewModel.addNewTag(id, label);
            }
        }).setMultipleChoiceEnabled(true); // Tags are allowed to be chosen multiple

        final LiveData<List<TagEntity>> tags = mEditViewModel.getAllTags();
        tags.observe(this,
                (List<TagEntity> tagEntities) -> {
                    if (DEBUG) {
                        Log.d(TAG, "Tags LiveData cb:" + tagEntities);
                    }
                    if (tagEntities != null && !tagEntities.isEmpty()) {
                        SparseArray<String> data = new SparseArray<>(tagEntities.size());
                        for (TagEntity entity : tagEntities) {
                            data.append(entity.getTagId(), entity.getTagName());
                        }
                        mGvTags.setData(data);
                    }

                    // we don't want it update automatically after we add new category
                    tags.removeObservers(this);
                });
    }

    private void updateDateTextView() {
        String format = getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = mCalendar.getTime();
        mTvDate.setText(sdf.format(date));
    }

    private void updateTimeTextView() {
        String format = getString(R.string.time_format);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = mCalendar.getTime();
        mTvTime.setText(sdf.format(date));
    }

    private void launchAddAccountFragment() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_edit_account);
        final LiveData<DataEntry> sharedData = getShareDataViewModel().getDataEntry();
        sharedData.observe(this, (DataEntry entry) -> {
            // new Account added callback
            if (DEBUG) {
                Log.d(TAG, "new Account added: " + entry.id + ", label=" + entry.label);
            }
            mGvAccounts.addNewEntry(entry);
            // we dont need to watch this data anymore, no pending callback expected
            sharedData.removeObservers(this);
        });
    }

    private boolean handleSaveExpense() {
        CharSequence amountStr = mTvAmount.getText();
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.amount)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        List<DataEntry> categories = mGvExpenseCategories.getSelectedData();
        if (categories == null || categories.isEmpty()) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.category)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        List<DataEntry> accounts = mGvAccounts.getSelectedData();
        if (accounts == null || accounts.isEmpty()) {
            Toast.makeText(getContext(),
                    getString(R.string.please_input_valid_format, getString(R.string.account)),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        RadioGroup rgExpenseType = mContentView.findViewById(R.id.rg_expense_type);

        List<DataEntry> tags = mGvTags.getSelectedData(); // it's allowed that no tags

        long id = System.currentTimeMillis();
        byte expenseType = Const.EXPENSE_TYPES.get(rgExpenseType.getCheckedRadioButtonId());
        Date dateTime = mCalendar.getTime();
        int expenseCategoryId = categories.get(0).id;
        int accountId = accounts.get(0).id;
        int amount = Integer.parseInt(amountStr.toString());

        int[] tagIds = null;
        if (!tags.isEmpty()) {
            tagIds = new int[tags.size()];
            for (int i = 0; i < tagIds.length; i++) {
                tagIds[i] = tags.get(i).id;
            }
        }

        mEditViewModel.addNewExpense(id, expenseType, dateTime, accountId,
                expenseCategoryId, amount, null, tagIds);
        return true;
    }
}