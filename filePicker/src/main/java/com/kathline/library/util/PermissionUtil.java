package com.kathline.library.util;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.kathline.library.R;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private static final String TAG = "PermissionsUtil";

    private PermissionFragment fragment;

    private static PermissionUtil mInstance;

    public static PermissionUtil getInstance() {
        if(mInstance == null) {
            synchronized (PermissionUtil.class) {
                if(mInstance == null) {
                    mInstance = new PermissionUtil();
                }
            }
        }
        return mInstance;
    }

    public PermissionUtil with(@NonNull FragmentActivity activity) {
        fragment = getPermissionsFragment(activity);
        return this;
    }

    public PermissionUtil with(@NonNull Fragment fragmentX) {
        fragment = getPermissionsFragment(fragmentX);
        return this;
    }

    public void showDialogTips(String permission, DialogInterface.OnClickListener onDenied) {
        if(fragment.getContext() == null) {
            return;
        }
        PackageManager packageManager = fragment.getContext().getPackageManager();
        String permissionName;
        try {
            PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
            assert permissionInfo.group != null;
            PermissionGroupInfo permissionGroupInfo =
                    packageManager.getPermissionGroupInfo(permissionInfo.group, 0);
            permissionName = (String) permissionGroupInfo.loadLabel(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            permissionName = "";
        }
        AlertDialog alertDialog = new AlertDialog.Builder(fragment.getContext()).setTitle("权限被禁用").setMessage(
                String.format("您拒绝了相关权限，无法正常使用本功能。请前往 设置->应用管理->%s->权限管理中启用 %s 权限",
                        fragment.getContext().getString(R.string.app_name),
                        permissionName
                )).setCancelable(false).
                setNegativeButton("返回", onDenied).
                setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent manageAppIntent = new Intent();
                        manageAppIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //第二个参数为包名
                        Uri uri = Uri.fromParts("package", fragment.getContext().getPackageName(), null);
                        manageAppIntent.setData(uri);
                        fragment.getContext().startActivity(manageAppIntent);
                    }
                }).create();
        alertDialog.show();

    }

    private PermissionFragment getPermissionsFragment(FragmentActivity activity) {
        PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitNow();
        }

        return fragment;
    }

    private PermissionFragment getPermissionsFragment(Fragment fragmentX) {
        PermissionFragment fragment = (PermissionFragment) fragmentX.getChildFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new PermissionFragment();
            FragmentManager fragmentManager = fragmentX.getChildFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitNow();
        }

        return fragment;
    }

    /**
     * 外部调用申请权限
     * @param permissions 申请的权限
     * @param listener 监听权限接口
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        fragment.setListener(listener);
        fragment.requestPermissions(permissions);
    }

    public interface PermissionListener {
        void onGranted();

        void onDenied(List<String> deniedPermission);

        void onShouldShowRationale(List<String> deniedPermission);
    }

    public static class PermissionFragment extends Fragment {
        /**
         * 申请权限的requestCode
         */
        private static final int PERMISSIONS_REQUEST_CODE = 1;

        /**
         * 权限监听接口
         */
        private PermissionListener listener;
        public void setListener(PermissionListener listener) {
            this.listener = listener;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        /**
         * 申请权限
         * @param permissions 需要申请的权限
         */
        public void requestPermissions(@NonNull String[] permissions) {
            List<String> requestPermissionList = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //找出所有未授权的权限
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionList.add(permission);
                    }
                }
                if (requestPermissionList.isEmpty()) {
                    //已经全部授权
                    permissionAllGranted();
                } else {
                    //申请授权
                    requestPermissions(requestPermissionList.toArray(new String[requestPermissionList.size()]), PERMISSIONS_REQUEST_CODE);
                }
            }else {
                //已经全部授权
                permissionAllGranted();
            }

        }

        /**
         * fragment回调处理权限的结果
         * @param requestCode 请求码 要等于申请时候的请求码
         * @param permissions 申请的权限
         * @param grantResults 对应权限的处理结果
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode != PERMISSIONS_REQUEST_CODE) {
                return;
            }

            if (grantResults.length > 0) {
                List<String> deniedPermissionList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissionList.add(permissions[i]);
                    }
                }

                if (deniedPermissionList.isEmpty()) {
                    //已经全部授权
                    permissionAllGranted();
                } else {

                    //勾选了对话框中”Don’t ask again”的选项, 返回false
                    for (String deniedPermission : deniedPermissionList) {
                        boolean flag = shouldShowRequestPermissionRationale(deniedPermission);
                        if (!flag) {
                            //拒绝授权
                            permissionShouldShowRationale(deniedPermissionList);
                            return;
                        }
                    }
                    //拒绝授权
                    permissionHasDenied(deniedPermissionList);

                }


            }

        }


        /**
         * 权限全部已经授权
         */
        private void permissionAllGranted() {
            if (listener != null) {
                listener.onGranted();
            }
        }

        /**
         * 有权限被拒绝
         *
         * @param deniedList 被拒绝的权限
         */
        private void permissionHasDenied(List<String> deniedList) {
            if (listener != null) {
                listener.onDenied(deniedList);
            }
        }

        /**
         * 权限被拒绝并且勾选了不在询问
         *
         * @param deniedList 勾选了不在询问的权限
         */
        private void permissionShouldShowRationale(List<String> deniedList) {
            if (listener != null) {
                listener.onShouldShowRationale(deniedList);
            }
        }
    }

}
