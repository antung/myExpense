package com.antang.myexpense.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antang.myexpense.R;
import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.ui.BaseFragment;
import com.antang.myexpense.utils.Const;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewExpenseFragment extends BaseFragment {
    private Spinner mYearFilter;
    private RecyclerView mExpenseDetailListView;
    private SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_expenses, container, false);
        mYearFilter = root.findViewById(R.id.tv_view_expense_year_filter);
        mExpenseDetailListView = root.findViewById(R.id.expense_detail_view_list);

        init();
        return root;
    }

    @Override
    public void onConfigActionButton(TextView actionButton) {
        actionButton.setVisibility(View.GONE);
    }

    private void init() {
        ArrayAdapter<String> yearFilterAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        yearFilterAdapter.add(getString(R.string.all_years));
        mYearFilter.setAdapter(yearFilterAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mExpenseDetailListView.setLayoutManager(layoutManager);


        ViewExpenseViewModel viewExpenseViewModel = mViewModelProvider.get(ViewExpenseViewModel.class);
        viewExpenseViewModel.getAllDetailedExpenses().observe(this, (List<ExpenseDetailView> l) -> {
            ExpenseDetailViewAdapter adapter = new ExpenseDetailViewAdapter(l);
            mExpenseDetailListView.setAdapter(adapter);
        });
    }

    private class ExpenseDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvDateTime;
        public TextView mTvExpenseType;
        public TextView mTvAmount;
        public TextView mTvExpenseCategory;
        public TextView mTvAccount;
        public TextView mTvTags;

        public ExpenseDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDateTime = itemView.findViewById(R.id.tv_expense_detail_datetime);
            mTvExpenseType = itemView.findViewById(R.id.tv_expense_detail_type);
            mTvAmount = itemView.findViewById(R.id.tv_expense_detail_amount);
            mTvExpenseCategory = itemView.findViewById(R.id.tv_expense_detail_category);
            mTvAccount = itemView.findViewById(R.id.tv_expense_detail_account);
            mTvTags = itemView.findViewById(R.id.tv_expense_detail_tags);
        }
    }
    private class ExpenseDetailViewAdapter extends RecyclerView.Adapter<ExpenseDetailViewHolder> {
        private List<ExpenseDetailView> expenseList;

        public ExpenseDetailViewAdapter(List<ExpenseDetailView> expenseList) {
            this.expenseList = expenseList;
        }

        @NonNull
        @Override
        public ExpenseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_detail_item, parent, false);
            return new ExpenseDetailViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseDetailViewHolder holder, int position) {
            ExpenseDetailView expenseInfo = expenseList.get(position);
            Date date = new Date(expenseInfo.dateTime);
            holder.mTvDateTime.setText(mSDF.format(date));
            holder.mTvExpenseType.setText(
                    expenseInfo.expenseType == Const.EXPENSE_TYPE_INCOME ?
                            getText(R.string.income) : getText(R.string.outcome));
            holder.mTvAccount.setText(String.valueOf(expenseInfo.amount));
            holder.mTvExpenseCategory.setText(expenseInfo.expenseCategoryName);
            holder.mTvAccount.setText(expenseInfo.accountDisplayName);
        }

        @Override
        public int getItemCount() {
            return expenseList != null ? expenseList.size() : 0;
        }
    }
}