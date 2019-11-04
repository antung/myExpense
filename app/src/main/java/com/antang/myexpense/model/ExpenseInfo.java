package com.antang.myexpense.model;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class ExpenseInfo {

    /**
     *                [income],      [outcome]
     *                  |             |
     *  -----------------            --------------
     *  |        |      |            |            |
     *  Salary   Bonus  ...          Home rent    Grocery
     *                                             |
     *                                          ------
     *                                          |
     *                                          Food
     * */
    public static class CategoricalAmount {
        public int amount;
        public int id;
        public String label;
        public CategoricalAmount parent;
        public SparseArray<CategoricalAmount> children;

        public CategoricalAmount() {

        }

        public CategoricalAmount(int id, String label, int amount) {
            this.id = id;
            this.label = label;
            this.amount = amount;
        }

        public void addChild(int id, String label, int amount) {
            CategoricalAmount ca = new CategoricalAmount(id, label, amount);
            ca.parent = this;
            if (children == null) {
                children = new SparseArray<>();
            }
            children.append(id, ca);
        }

        public void clear() {
            if (children != null) {
                children.clear();
            }
        }
    }
    public int beginningBalance;
    public CategoricalAmount data = new CategoricalAmount();
    public CategoricalAmount anchor = data;
}
