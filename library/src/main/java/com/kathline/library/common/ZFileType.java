package com.kathline.library.common;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;

public abstract class ZFileType {
    /**
     * 打开文件
     * @param filePath  文件路径
     * @param view      当前视图
     */
    public abstract void openFile(String filePath, View view);

    /**
     * 加载文件
     * @param filePath 文件路径
     * @param pic      文件展示的图片
     */
    public abstract void loadingFile(String filePath, ImageView pic);

    /**
     * 文件详情
     */
    public void infoFile(ZFileBean bean, Context context) {
        ZFileContent.getZFileHelp().getFileOperateListener().fileInfo(bean, context);
    }

    protected final int getRes(int res, int defaultRes) {
        return res == -1 ? defaultRes : res;
    }
}
