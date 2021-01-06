package com.kathline.library.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kathline.library.R;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.content.ZFileContent;

public class ZFileRenameDialog extends ZFileManageDialog implements Runnable {

    private Handler handler;

    private EditText zfileDialogRenameEdit;
    private Button zfileDialogRenameDown;
    private Button zfileDialogRenameCancel;

    private void initView() {
        zfileDialogRenameEdit = (EditText) view.findViewById(R.id.zfile_dialog_renameEdit);
        zfileDialogRenameDown = (Button) view.findViewById(R.id.zfile_dialog_rename_down);
        zfileDialogRenameCancel = (Button) view.findViewById(R.id.zfile_dialog_rename_cancel);
    }

    public interface ReanameDownListener {
        void invoke(String name);
    }

    private ReanameDownListener reanameDown;

    public void setReanameDownListener(ReanameDownListener listener) {
        reanameDown = listener;
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_zfile_rename;
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
        initView();
        handler = new Handler();
        zfileDialogRenameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (v.getText().toString().isEmpty()) {
                        ZFileContent.toast(v, "请输入文件名");
                    } else {
                        if(reanameDown != null) {
                            reanameDown.invoke(v.getText().toString());
                        }
                        dismiss();
                    }
                }
                return true;
            }
        });
        zfileDialogRenameDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reanameDown != null) {
                    reanameDown.invoke(zfileDialogRenameEdit.getText().toString());
                    dismiss();
                }
            }
        });
        zfileDialogRenameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ZFileContent.setNeedWH(this);
        if(handler != null) {
            handler.postDelayed(this, 150);
        }
    }

    @Override
    public void run() {
        zfileDialogRenameEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.showSoftInput(zfileDialogRenameEdit, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDestroyView() {
        closeKeyboard();
        if(handler != null) {
            handler.removeCallbacks(this);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroyView();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            if(imm.isActive()) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
