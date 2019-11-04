package com.antang.myexpense.utils;

import android.util.SparseArray;

import com.antang.myexpense.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Const {
    public static final byte TYPE_INCOME = 100;
    public static final byte TYPE_OUTCOME = 101;

    public static final SparseArray<Integer> LABEL_MAP = new SparseArray<Integer>() {{
        append(1, R.string.jan);
        append(2, R.string.feb);
        append(3, R.string.mar);
        append(4, R.string.apr);
        append(5, R.string.may);
        append(6, R.string.jun);
        append(7, R.string.jul);
        append(8, R.string.aug);
        append(9, R.string.sep);
        append(10, R.string.oct);
        append(11, R.string.nov);
        append(12, R.string.dec);
        append(TYPE_INCOME, R.string.income);
        append(TYPE_OUTCOME, R.string.outcome);
    }};

    public static final List<int[]> BG_DRAWABLES =
            new ArrayList<int[]>() {{
                add(new int[]{R.drawable.item_bg_light_blue, R.drawable.item_bg_dark_blue});
                add(new int[]{R.drawable.item_bg_light_red, R.drawable.item_bg_dark_red});
                add(new int[]{R.drawable.item_bg_ligth_yellow, R.drawable.item_bg_dark_yellow});
            }};

    public static final int ID_ADD_NEW = "_add.new_".hashCode();

    public static byte EXPENSE_TYPE_INCOME = 1;
    public static byte EXPENSE_TYPE_OUTCOME = 2;
    public static SparseArray<Byte> EXPENSE_TYPES = new SparseArray<Byte>(4) {{
        append(R.id.rb_income, EXPENSE_TYPE_INCOME);
        append(R.id.rb_outcome, EXPENSE_TYPE_OUTCOME);
    }};
}
