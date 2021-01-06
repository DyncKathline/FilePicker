package com.kathline.fileoperatorjava.diy;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kathline.fileoperatorjava.R;
import com.kathline.library.common.ZFileType;

/**
 * 自定义Apk文件类型
 */
public class ApkType extends ZFileType {

    /**
     * 打开文件
     * @param filePath  文件路径
     * @param view      当前视图
     */
    @Override
    public void openFile(String filePath, View view) {
        Toast.makeText(view.getContext(), "打开自定义拓展文件", Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载文件
     * @param filePath 文件路径
     * @param pic      文件展示的图片
     */
    @Override
    public void loadingFile(String filePath, ImageView pic) {
        // 获取PackageManagerAPK的信息
        try {
            PackageManager packageManager = pic.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                pic.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));
            }
        } catch (Throwable ignore) {
            pic.setImageResource(R.mipmap.ic_launcher_round);
        }
    }
}