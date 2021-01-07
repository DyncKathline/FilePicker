package com.kathline.library.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kathline.library.content.ZFileContent;

public final class ZFileLog {
    private static final int I = 0;
    private static final int E = 1;

    public static final void i(@Nullable String msg) {
        i("ZFileManager", msg);
    }

    public static final void e(@Nullable String msg) {
        e("ZFileManager", msg);
    }

    public static final void i(@NonNull String tag, @Nullable String message) {
        log(0, tag, message);
    }

    public static final void e(@NonNull String tag, @Nullable String message) {
        log(1, tag, message);
    }

    private static final void log(int type, String TAG, String msg) {
        if (ZFileContent.getZFileConfig().isShowLog()) {
            String var10001;
            switch(type) {
                case 0:
                    var10001 = msg;
                    if (msg == null) {
                        var10001 = "Null";
                    }

                    Log.i(TAG, var10001);
                    break;
                case 1:
                    var10001 = msg;
                    if (msg == null) {
                        var10001 = "Null";
                    }

                    Log.e(TAG, var10001);
            }
        }

    }

    private ZFileLog() {
    }
}

