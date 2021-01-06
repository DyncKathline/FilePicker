package com.kathline.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.kathline.library.content.ZFileContent;

import java.io.File;

public final class ZFileOpenUtil {
    private static final String TXT = "text/plain";
    private static final String ZIP = "application/x-zip-compressed";
    private static final String DOC = "application/msword";
    private static final String XLS = "application/vnd.ms-excel";
    private static final String PPT = "application/vnd.ms-powerpoint";
    private static final String PDF = "application/pdf";

    public static final void openTXT(@NonNull String filePath, @NonNull View view) {
        open(filePath, TXT, view.getContext());
    }

    public static final void openZIP(@NonNull String filePath, @NonNull View view) {
        open(filePath, ZIP, view.getContext());
    }

    public static final void openDOC(@NonNull String filePath, @NonNull View view) {
        open(filePath, DOC, view.getContext());
    }

    public static final void openXLS(@NonNull String filePath, @NonNull View view) {
        open(filePath, XLS, view.getContext());
    }

    public static final void openPPT(@NonNull String filePath, @NonNull View view) {
        open(filePath, PPT, view.getContext());
    }

    public static final void openPDF(@NonNull String filePath, @NonNull View view) {
        open(filePath, PDF, view.getContext());
    }

    private static final void open(String filePath, String type, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri = FileProvider.getUriForFile(context, ZFileContent.getZFileConfig().getAuthority(), new File(filePath));
                intent.setDataAndType(contentUri, type);
            } else {
                contentUri = Uri.fromFile(new File(filePath));
                intent.setDataAndType(contentUri, type);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            ZFileLog.e("ZFileConfiguration.authority 未设置？？？");
            ZFileContent.toast(context, "文件类型可能不匹配或找不到打开该文件类型的程序，打开失败");
        }

    }

    private ZFileOpenUtil() {
    }
}

