package com.antang.myexpense.model;

import java.util.ArrayList;
import java.util.List;

public class TotalExpenseInfo extends ExpenseInfo {
    public int beginningYear; // 0 means all years
    public int endingYear;
    public List<MonthlyExpenseInfo> monthlyExpenseInfoList = new ArrayList<>(12);
}
