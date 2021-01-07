package com.kathline.library.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kathline.library.R;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.content.ZFileContent;

public class ZFileSortDialog extends ZFileManageDialog implements RadioGroup.OnCheckedChangeListener {

    private int sortSelectId = 0;
    private int sequenceSelectId = 0;

    private RadioGroup zfileSortGroup;
    private RadioButton zfileSortByDefault;
    private RadioButton zfileSortByName;
    private RadioButton zfileSortByDate;
    private RadioButton zfileSortBySize;
    private LinearLayout zfileSequenceLayout;
    private RadioGroup zfileSequenceGroup;
    private RadioButton zfileSequenceAsc;
    private RadioButton zfileSequenceDesc;
    private Button zfileDialogSortDown;
    private TextView zfileDialogSortCancel;

    private void initView() {
        zfileSortGroup = (RadioGroup) view.findViewById(R.id.zfile_sortGroup);
        zfileSortByDefault = (RadioButton) view.findViewById(R.id.zfile_sort_by_default);
        zfileSortByName = (RadioButton) view.findViewById(R.id.zfile_sort_by_name);
        zfileSortByDate = (RadioButton) view.findViewById(R.id.zfile_sort_by_date);
        zfileSortBySize = (RadioButton) view.findViewById(R.id.zfile_sort_by_size);
        zfileSequenceLayout = (LinearLayout) view.findViewById(R.id.zfile_sequenceLayout);
        zfileSequenceGroup = (RadioGroup) view.findViewById(R.id.zfile_sequenceGroup);
        zfileSequenceAsc = (RadioButton) view.findViewById(R.id.zfile_sequence_asc);
        zfileSequenceDesc = (RadioButton) view.findViewById(R.id.zfile_sequence_desc);
        zfileDialogSortDown = (Button) view.findViewById(R.id.zfile_dialog_sort_down);
        zfileDialogSortCancel = (TextView) view.findViewById(R.id.zfile_dialog_sort_cancel);
    }

    public interface CheckedChangedListener {
        void onCheckedChange(int sortSelectId, int sequenceSelectId);
    }

    private CheckedChangedListener checkedChangedListener;

    public void setCheckedChangedListener(CheckedChangedListener listener) {
        checkedChangedListener = listener;
    }

    public static ZFileSortDialog newInstance(int sortSelectId, int sequenceSelectId) {
        Bundle bundle = new Bundle();
        bundle.putInt("sortSelectId", sortSelectId);
        bundle.putInt("sequenceSelectId", sequenceSelectId);
        ZFileSortDialog fragment = new ZFileSortDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.zfile_sortGroup) { // 方式
            sortSelectId = checkedId;
            check();
        } else { // 顺序
            sequenceSelectId = checkedId;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_zfile_sort;
    }

    @NonNull
    @Override
    public Dialog createDialog(@Nullable Bundle bundle) {
        Dialog dialog = new Dialog(getContext(), R.style.ZFile_Common_Dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        return dialog;
    }

    @Override
    public void init(@Nullable Bundle bundle) {
        if (getArguments() != null) {
            sortSelectId = getArguments().getInt("sortSelectId", 0);
            sequenceSelectId = getArguments().getInt("sequenceSelectId", 0);
        }
        initView();
        check();
        if (sortSelectId == R.id.zfile_sort_by_default) {
            zfileSortByDefault.setChecked(true);
        } else if (sortSelectId == R.id.zfile_sort_by_name) {
            zfileSortByName.setChecked(true);
        } else if (sortSelectId == R.id.zfile_sort_by_date) {
            zfileSortByDate.setChecked(true);
        } else if (sortSelectId == R.id.zfile_sort_by_size) {
            zfileSortBySize.setChecked(true);
        } else {
            zfileSortByDefault.setChecked(true);
        }
        if(sequenceSelectId == R.id.zfile_sequence_asc) {
            zfileSequenceAsc.setChecked(true);
        }else if(sequenceSelectId == R.id.zfile_sequence_desc) {
            zfileSequenceDesc.setChecked(true);
        }else {
            zfileSequenceAsc.setChecked(true);
        }
        zfileSortGroup.setOnCheckedChangeListener(this);
        zfileSequenceGroup.setOnCheckedChangeListener(this);
        zfileDialogSortCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        zfileDialogSortDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedChangedListener != null) {
                    checkedChangedListener.onCheckedChange(sortSelectId, sequenceSelectId);
                }
                dismiss();
            }
        });
    }

    private void check() {
        zfileSequenceLayout.setVisibility((sortSelectId == R.id.zfile_sort_by_default) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ZFileContent.setNeedWH(this);
    }
}
