package com.kathline.library.common;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.kathline.library.Function1;

import java.lang.ref.SoftReference;

public class ZFileCommonDialog {

    private SoftReference<Context> reference;
    private boolean isUserDefaultTitle;

    public ZFileCommonDialog(Context context) {
        this(context, true);
    }

    public ZFileCommonDialog(Context context, boolean isUserDefaultTitle) {
        reference = new SoftReference<>(context);
        this.isUserDefaultTitle = isUserDefaultTitle;
    }

    private AlertDialog.Builder createDialog(final Function1 listener, String[] str) {
        if(reference.get() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
            if(isUserDefaultTitle) {
                builder.setTitle("温馨提示");
                builder.setMessage(str[0]);
                builder.setPositiveButton(str[1], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.invoke();
                    }
                });
            }else {
                builder.setTitle(str[0]);
                builder.setMessage(str[1]);
                builder.setPositiveButton(str[2], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.invoke();
                    }
                });
            }
            builder.setCancelable(false);
            return builder;
        }else {
            return null;
        }
    }

    public void showDialog1(Function1 listener, String... str) {
        AlertDialog.Builder dialog = createDialog(listener, str);
        if(dialog != null) {
            dialog.show();
        }
    }

    public void showDialog2(Function1 listener1, final Function1 listener2, String... str) {
        AlertDialog.Builder dialog = createDialog(listener1, str);
        if(dialog != null) {
            dialog.setNegativeButton(isUserDefaultTitle ? str[2] : str[3], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if(listener2 != null) {
                        listener2.invoke();
                    }
                }
            });
            dialog.show();
        }
    }

    public void showDialog3(Function1 listener1, final Function1 listener2, final Function1 listener3, String... str) {
        AlertDialog.Builder dialog = createDialog(listener1, str);
        if(dialog != null) {
            dialog.setNegativeButton(isUserDefaultTitle ? str[2] : str[3], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener2.invoke();
                }
            });
            dialog.setNeutralButton(isUserDefaultTitle ? str[3] : str[4], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener3.invoke();
                }
            });
            dialog.show();
        }
    }
}
