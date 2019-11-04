package com.antang.myexpense.ui.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.antang.myexpense.R;
import com.antang.myexpense.ui.DataEntry;
import com.antang.myexpense.utils.Const;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleTextGridView extends RecyclerView {

    private static final int VIEW_TYPE_LAUNCH_EDIT = 1;
    private static final int VIEW_TYPE_EDIT_VIEW = 2;
    private static final int VIEW_TYPE_CHECKABLE_VIEW = 3;

    private SparseArray<String> mData;
    private boolean mEditing;
    private SimpleTextGridViewAdapter mAdapter = new SimpleTextGridViewAdapter();
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;
    private boolean mIsMultipleChoice; // single choice by default
    private SimpleTextGridViewListener mListener;
    private GridLayoutManager mLayoutManager;
    private int mItemOffset;

    public interface SimpleTextGridViewListener {
        default public void onNewEntryAdded(int id, String label) {};

        default public boolean onInterceptAddNewAction() {
            return false;
        };
    }

    public SimpleTextGridView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleTextGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTextGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mItemOffset = context.getResources().getDimensionPixelOffset(R.dimen.item_view_margin);
        final int columns = context.getResources().getInteger(R.integer.num_of_column_simple_text_grid);
        mLayoutManager = new GridLayoutManager(context, columns);

        // let edit mode view occupied the entire 1st row
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 && mEditing) {
                    return columns;
                } else {
                    return 1;
                }
            }
        });

        setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull State state) {

                outRect.left = mItemOffset;
                outRect.right = mItemOffset;
                outRect.bottom = mItemOffset;
                outRect.top = mItemOffset;
            }
        };
        addItemDecoration(itemDecoration);
    }

    public void setData(SparseArray<String> data) {
        mData = data;
        mAdapter.setData(data);
        if (getAdapter() == null) {
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public SimpleTextGridView setMultipleChoiceEnabled(boolean enabled) {
        mIsMultipleChoice = enabled;
        return this;
    }

    public SimpleTextGridView setListener(SimpleTextGridViewListener listener) {
        mListener = listener;
        return this;
    }

    public List<DataEntry> getSelectedData() {
        return mAdapter.getSelectedData();
    }
    
    public void addNewEntry(DataEntry newEntry) {
        if (newEntry == null) {
            return;
        }

        mAdapter.addNewDataEntry(newEntry);
    }

    private void handleAddNewSimpleTextEntry(String newLabel) {
        if (TextUtils.isEmpty(newLabel)) {
            return;
        }

        final int id = newLabel.hashCode();
        if (mData.get(id) == null) {
            if (mListener != null) {
                mListener.onNewEntryAdded(id, newLabel);
            }
            DataEntry newEntry = new DataEntry(id, newLabel);
            mAdapter.addNewDataEntry(newEntry);
        } else {
            Context context = getContext();
            Toast.makeText(context, context.getText(R.string.item_already_existed), Toast.LENGTH_LONG).show();
        }
    }

    private class SimpleTextViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView mCheckedTextView;
        public EditText mEditTextView;

        public SimpleTextViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case VIEW_TYPE_EDIT_VIEW:
                    mEditTextView = itemView.findViewById(R.id.edit_add_new);
                    itemView.findViewById(R.id.edit_btn_save).setOnClickListener((v) -> {
                        mEditing = false;
                        handleAddNewSimpleTextEntry(mEditTextView.getText().toString());
                    });

                    itemView.findViewById(R.id.edit_btn_cancel).setOnClickListener((v) -> {
                        mEditing = false;
                        mAdapter.notifyDataSetChanged();
                    });

                    break;
                case VIEW_TYPE_LAUNCH_EDIT:
                    itemView.setOnClickListener((v) -> {
                        if (mListener != null && mListener.onInterceptAddNewAction()) {
                            return;
                        }

                        if (!mEditing) {
                            mEditing = true;
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case VIEW_TYPE_CHECKABLE_VIEW:
                    mCheckedTextView = (CheckedTextView) itemView;
                    mCheckedTextView.setChecked(true);
                    itemView.setOnClickListener((v) -> {
                        CheckedTextView ctv = (CheckedTextView) v;
                        ctv.toggle();
                        DataEntry dataEntry = (DataEntry) v.getTag();
                        if (dataEntry != null) {
                            dataEntry.checked = ctv.isChecked();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private class SimpleTextGridViewAdapter extends RecyclerView.Adapter<SimpleTextViewHolder> {
        private List<DataEntry> mDataEntryList;

        public void setData(SparseArray<String> data) {
            mDataEntryList = new ArrayList<>();
            mDataEntryList.add(new DataEntry(Const.ID_ADD_NEW, null));
            if (data != null) {
                final int size = data.size();
                for (int i = 0; i < size; i++) {
                    mDataEntryList.add(new DataEntry(data.keyAt(i), data.valueAt(i)));
                }
            }
        }

        void updateDataEntrySelectStatus(DataEntry newSelected) {
            newSelected.checked = true;
            if (!mIsMultipleChoice) {
                for (DataEntry entry : mDataEntryList) {
                    if (entry != newSelected) {
                        entry.checked = false;
                    }
                }
            }
        }

        void addNewDataEntry(DataEntry newAdded) {
            mDataEntryList.add(newAdded);
            updateDataEntrySelectStatus(newAdded);
            notifyDataSetChanged();
        }

        List<DataEntry> getSelectedData() {
            List<DataEntry> selected = new ArrayList<>();
            for (DataEntry entry : mDataEntryList) {
                if (entry.checked) {
                    selected.add(entry);
                }
            }
            return selected;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return mEditing ? VIEW_TYPE_EDIT_VIEW : VIEW_TYPE_LAUNCH_EDIT;
            } else {
                return VIEW_TYPE_CHECKABLE_VIEW;
            }
        }

        @NonNull
        @Override
        public SimpleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layoutId = 0;
            switch (viewType) {
                case VIEW_TYPE_EDIT_VIEW:
                    layoutId = R.layout.simple_edit_text_view_item;
                    break;
                case VIEW_TYPE_LAUNCH_EDIT:
                    layoutId = R.layout.simple_add_new_text_view_item;
                    break;
                case VIEW_TYPE_CHECKABLE_VIEW:
                    layoutId = R.layout.simple_checked_text_view_item;
                    break;
                default:
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new SimpleTextViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleTextViewHolder holder, int position) {
            if (holder.mCheckedTextView != null) {
                DataEntry dataEntry = mDataEntryList.get(position);
                holder.mCheckedTextView.setText(dataEntry.label);
                holder.mCheckedTextView.setTag(mDataEntryList.get(position));
                holder.mCheckedTextView.setChecked(dataEntry.checked);
            }
        }

        @Override
        public int getItemCount() {
            return mDataEntryList != null ? mDataEntryList.size() : 0;
        }
    };
}
