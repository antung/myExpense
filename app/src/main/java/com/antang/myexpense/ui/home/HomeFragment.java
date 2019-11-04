package com.antang.myexpense.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antang.myexpense.Config;
import com.antang.myexpense.R;

import com.antang.myexpense.db.databaseview.ExpenseDetailView;
import com.antang.myexpense.model.ExpenseInfo;
import com.antang.myexpense.model.MonthlyExpenseInfo;
import com.antang.myexpense.model.TotalExpenseInfo;
import com.antang.myexpense.ui.BaseFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private static final String TAG = Config.TAG_PREFIX + "HomeFragment";
    private static final boolean DEBUG = Config.DEBUG;

    private PieChart mExpenseChart;
    private Spinner mYearFilter;
    private RecyclerView mMonthlyListView;
    private TotalExpenseInfo mTotalExpenseInfo;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) {
            Log.d(TAG, "onActivityCreated");
        }
    }

    @Override
    public void onConfigActionButton(TextView actionButton) {
        actionButton.setVisibility(View.GONE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (DEBUG) {
            Log.d(TAG, "onActivityCreated");
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mExpenseChart = root.findViewById(R.id.home_expense_chart);
        mYearFilter = root.findViewById(R.id.tv_home_year_filter);
        mMonthlyListView = root.findViewById(R.id.home_monthly_view);

        init();

        final HomeViewModel viewModel = mViewModelProvider.get(HomeViewModel.class);
        viewModel.getExpenseStatistics(this, -1).
                observe(this, (TotalExpenseInfo totalExpenseInfo) -> {
            updateExpenseUI(totalExpenseInfo);
        });

        return root;
    }

    @Override
    public boolean onLogoButtonClicked() {
        return false;
    }

    private void init() {
        PieDataSet dataSet = new PieDataSet(null, null);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setHighlightEnabled(true);
        PieData data = new PieData(dataSet);
        mExpenseChart.setData(data);
        mExpenseChart.setCenterText(getString(R.string.app_name));
        mExpenseChart.getDescription().setEnabled(false);
        mExpenseChart.setDrawMarkers(false);
        mExpenseChart.invalidate();
        mExpenseChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected:" + e + ", highlight=" + h);
            }

            @Override
            public void onNothingSelected() {
                Log.d(TAG, "onNothingSelected");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMonthlyListView.setLayoutManager(layoutManager);
        MonthlyViewAdapter adapter = new MonthlyViewAdapter();
        mMonthlyListView.setAdapter(adapter);

        ArrayAdapter<String> yearFilterAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        yearFilterAdapter.add(getString(R.string.all_years));
        mYearFilter.setAdapter(yearFilterAdapter);

    }

    private void updateExpenseUI(TotalExpenseInfo totalExpenseInfo) {
        mTotalExpenseInfo = totalExpenseInfo;
        PieData data = mExpenseChart.getData();
        IPieDataSet dataSet = data.getDataSet();
        dataSet.clear();
        if (totalExpenseInfo != null) {
            if (totalExpenseInfo.data != null && totalExpenseInfo.data.children != null) {
                final int size = totalExpenseInfo.data.children.size();
                for (int i = 0; i < size; i++) {
                    ExpenseInfo.CategoricalAmount ca = totalExpenseInfo.data.children.valueAt(i);
                    dataSet.addEntry(new PieEntry(ca.amount, ca.label));
                }
            } else {
                Log.i(TAG, "updateExpenseUI: data null." + totalExpenseInfo.data);
            }

            if (totalExpenseInfo.beginningYear > 0) {
                if (totalExpenseInfo.endingYear - totalExpenseInfo.beginningYear > 0) {
                    mExpenseChart.setCenterText(getString(R.string.from_year_to,
                            totalExpenseInfo.beginningBalance, totalExpenseInfo.endingYear));
                } else {
                    mExpenseChart.setCenterText(getString(R.string.year_x, totalExpenseInfo.beginningBalance));
                }
            } else {
                mExpenseChart.setCenterText(getString(R.string.all_years));
            }

            MonthlyViewAdapter adapter = (MonthlyViewAdapter)mMonthlyListView.getAdapter();
            adapter.setMonthlyExpenseInfoList(totalExpenseInfo.monthlyExpenseInfoList);
            adapter.notifyDataSetChanged();
        } else {
            mExpenseChart.setCenterText(getString(R.string.app_name));
        }

        mExpenseChart.setData(data); // to trigger to re-caculate angles, otherwise exception
        mExpenseChart.invalidate();
    }

    private class MonthlyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvLabel;
        public TextView mTvDetails;

        public MonthlyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvLabel = itemView.findViewById(R.id.tv_label);
            mTvDetails = itemView.findViewById(R.id.tv_details);
        }
    }
    private class MonthlyViewAdapter extends RecyclerView.Adapter<MonthlyViewHolder> {
        private List<MonthlyExpenseInfo> monthlyExpenseInfoList;

        public void setMonthlyExpenseInfoList(List<MonthlyExpenseInfo> monthlyExpenseInfoList) {
            this.monthlyExpenseInfoList = monthlyExpenseInfoList;
        }

        @NonNull
        @Override
        public MonthlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_monthly_view_item, parent, false);
            return new MonthlyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthlyViewHolder holder, int position) {
            MonthlyExpenseInfo expenseInfo = monthlyExpenseInfoList.get(position);
            holder.mTvLabel.setText(expenseInfo.label);
        }

        @Override
        public int getItemCount() {
            return monthlyExpenseInfoList != null ? monthlyExpenseInfoList.size() : 0;
        }
    }
}