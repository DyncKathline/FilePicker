package com.kathline.library.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class ZFileManageDialog extends DialogFragment {

    protected View view;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(this.getContentView(), container, false);
        return view;
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return this.createDialog(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.init(savedInstanceState);
        Dialog dialog = this.getDialog();
        if (dialog != null) {
            dialog.setOnKeyListener((DialogInterface.OnKeyListener) (new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        return onBackPressed();
                    } else {
                        return false;
                    }
                }
            }));

        }
    }

    public abstract int getContentView();

    @NonNull
    public abstract Dialog createDialog(@Nullable Bundle bundle);

    public abstract void init(@Nullable Bundle bundle);

    public boolean onBackPressed() {
        return false;
    }
}
