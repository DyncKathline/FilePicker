package com.kathline.library.async;

import android.content.Context;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.listener.ZFileListener;
import com.kathline.library.util.ZFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取 QQ 或 WeChat 文件 （QQ WeChat ---> QW）
 * @property fileType           QQ 或 WeChat
 * @property type               文件类型
 * @property filePathArray      过滤规则
 */
public final class ZFileQWAsync extends ZFileAsync {
    private ArrayList<String> filePathArray;
    private String fileType;
    private int type;

    protected void onPreExecute() {
        ZFileListener.QWFileLoadListener loadListener = ZFileContent.getZFileHelp().getQWFileLoadListener();
        List<String> list;
        if (loadListener != null) {
            list = loadListener.getQWFilePathArray(this.fileType, this.type);
        }else {
            list = getQWFilePathArray();
        }
        filePathArray.addAll(list);
    }

    @Override
    protected List<ZFileBean> doingWork(String[] filterArray) {
        ZFileListener.QWFileLoadListener loadListener = ZFileContent.getZFileHelp().getQWFileLoadListener();
        if (loadListener != null) {
            return loadListener.getQWFileDatas(type, filePathArray, filterArray);
        }else {
            return ZFileUtil.getQWFileData(type, filePathArray, filterArray);
        }
    }

    protected void onPostExecute() {
        filePathArray.clear();
    }

    private List<String> getQWFilePathArray() {
        ArrayList<String> listArray = new ArrayList<>();
        if (fileType.equals(ZFileConfiguration.QQ)) {
            switch(this.type) {
                case 0:
                    listArray.add("/storage/emulated/0/tencent/QQ_Images/");
                    listArray.add("/storage/emulated/0/Pictures/QQ/");
                    break;
                case 1:
                    listArray.add("/storage/emulated/0/Pictures/QQ/");
                    break;
                default:
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/");
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQ_business/");
            }
        } else {
            switch(this.type) {
                case 0:
                case 1:
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/");
                    break;
                default:
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/Download/");
            }
        }

        return listArray;
    }

    public ZFileQWAsync(String fileType, int type, Context context, CallBack block) {
        super(context, block);
        this.fileType = fileType;
        this.type = type;
        this.filePathArray = new ArrayList<>();
    }
}

