package com.kathline.library.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ZFilePermissionUtil {

    /** 读写SD卡权限  */
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_EXTERNAL_CODE = 0x1001;

    /**
     * 判断是否申请过权限
     * @param permissions   权限
     * @return true表示没有申请过
     */
    public static boolean hasPermission(Context context, String... permissions) {
        List<String> toApplyList = new ArrayList<>();
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    toApplyList.add(permission);
                }
            }
        }
        return toApplyList.size() == permissions.length;
    }

    public static void requestPermission(Activity activity, int code, String... requestPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, requestPermission, code);
        }

    }
    
}
