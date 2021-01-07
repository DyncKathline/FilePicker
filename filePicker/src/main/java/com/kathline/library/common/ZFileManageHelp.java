package com.kathline.library.common;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.listener.ZFileDefaultLoadListener;
import com.kathline.library.listener.ZFileListener;
import com.kathline.library.ui.ProxyFragment;
import com.kathline.library.ui.ProxyListener;
import com.kathline.library.ui.ZFileListActivity;
import com.kathline.library.ui.ZFileQWActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.kathline.library.content.ZFileContent.ERROR_MSG;
import static com.kathline.library.content.ZFileContent.QW_FILE_TYPE_KEY;

public class ZFileManageHelp {

    /**
     * 图片类型和视频类型的显示方式，必须手动实现 并在调用前或Application中初始化
     */
    private ZFileListener.ZFileImageListener imageLoadeListener;
    /**
     * 文件数据获取
     */
    private ZFileListener.ZFileLoadListener fileLoadListener = new ZFileDefaultLoadListener();

    /**
     * QQ or WeChat 文件获取
     */
    private ZFileListener.QWFileLoadListener qwLoadListener;
    /**
     * 文件类型
     */
    private ZFileListener.ZFileTypeListener fileTypeListener = new ZFileListener.ZFileTypeListener();
    /**
     * 文件操作
     */
    private ZFileListener.ZFileOperateListener fileOperateListener = new ZFileListener.ZFileOperateListener();
    /**
     * 打开默认支持的文件
     */
    private ZFileListener.ZFileOpenListener fileOpenListener = new ZFileListener.ZFileOpenListener();
    /**
     * 文件的相关配置信息
     */
    private ZFileConfiguration config = new ZFileConfiguration();

    public ZFileListener.ZFileImageListener getImageLoadListener() {
        if(imageLoadeListener == null) {
            throw new NullPointerException("ZFileImageListener is Null, You need call method \"init()\"");
        }
        return imageLoadeListener;
    }

    public ZFileManageHelp init(ZFileListener.ZFileImageListener imageLoadeListener) {
        this.imageLoadeListener = imageLoadeListener;
        return this;
    }

    public final ZFileListener.ZFileLoadListener getFileLoadListener() {
        return this.fileLoadListener;
    }

    public final ZFileManageHelp setFileLoadListener(ZFileListener.ZFileLoadListener fileLoadListener) {
        this.fileLoadListener = fileLoadListener;
        return this;
    }

    public final ZFileListener.QWFileLoadListener getQWFileLoadListener() {
        return this.qwLoadListener;
    }

    public final ZFileManageHelp setQWFileLoadListener(ZFileListener.QWFileLoadListener qwLoadListener) {
        this.qwLoadListener = qwLoadListener;
        return this;
    }

    public final ZFileListener.ZFileTypeListener getFileTypeListener() {
        return this.fileTypeListener;
    }

    public final ZFileManageHelp setFileTypeListener(ZFileListener.ZFileTypeListener fileTypeListener) {
        this.fileTypeListener = fileTypeListener;
        return this;
    }

    public final ZFileListener.ZFileOperateListener getFileOperateListener() {
        return this.fileOperateListener;
    }

    public final ZFileManageHelp setFileOperateListener(ZFileListener.ZFileOperateListener fileOperateListener) {
        this.fileOperateListener = fileOperateListener;
        return this;
    }

    public final ZFileListener.ZFileOpenListener getFileOpenListener() {
        return this.fileOpenListener;
    }

    public final ZFileManageHelp setFileOpenListener(ZFileListener.ZFileOpenListener fileOpenListener) {
        this.fileOpenListener = fileOpenListener;
        return this;
    }

    public final ZFileConfiguration getConfiguration() {
        return this.config;
    }

    public final ZFileManageHelp setConfiguration(ZFileConfiguration config) {
        this.config = config;
        return this;
    }

    /**
     * 获取返回的数据
     */
    public List<ZFileBean> getSelectData(int requestCode, int resultCode, Intent data) {
        List<ZFileBean> list = new ArrayList<>();
        if (requestCode == ZFileContent.ZFILE_REQUEST_CODE && resultCode == ZFileContent.ZFILE_RESULT_CODE) {
            list = data.getParcelableArrayListExtra(ZFileContent.ZFILE_SELECT_DATA_KEY);
        }
        return list;
    }

    public final void start(Object fragmentOrActivity) {
        start(fragmentOrActivity, null);
    }

    /**
     * 跳转至文件管理页面，如果listener为null，则在当前页面的onActivityResult()方法中回调，否则在listener中回调
     * @param fragmentOrActivity
     * @param listener              链式回调数据
     */
    public final void start(Object fragmentOrActivity, ProxyListener listener) {
        //开启代理
        ProxyFragment fragment = null;
        if(listener != null && fragmentOrActivity instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) fragmentOrActivity;
            fragment = ProxyFragment.beginRequest(activity, ZFileContent.ZFILE_REQUEST_CODE, listener);
        }else if(listener != null && fragmentOrActivity instanceof Fragment) {
            Fragment f = (Fragment) fragmentOrActivity;
            fragment = ProxyFragment.beginRequest(f, ZFileContent.ZFILE_REQUEST_CODE, listener);
        }
        switch (getConfiguration().getFilePath()) {
            case ZFileConfiguration.QQ:
                startByQQ(fragment != null ? fragment : fragmentOrActivity);
                break;
            case ZFileConfiguration.WECHAT:
                startByWechat(fragment != null ? fragment : fragmentOrActivity);
                break;
            default:
                startByFileManager(fragment != null ? fragment : fragmentOrActivity, getConfiguration().getFilePath());
        }
    }

    private void startByQQ(Object fragmentOrActivity) {
        ArrayMap<String, Object> map = getMap();
        if(fragmentOrActivity instanceof Activity) {
            Activity activity = (Activity) fragmentOrActivity;
            map.put(ZFileContent.QW_FILE_TYPE_KEY, ZFileConfiguration.QQ);
            ZFileContent.jumpActivity(activity, ZFileQWActivity.class, map);
        }else if(fragmentOrActivity instanceof Fragment) {
            Fragment fragment = (Fragment) fragmentOrActivity;
            map.put(ZFileContent.QW_FILE_TYPE_KEY, ZFileConfiguration.QQ);
            ZFileContent.jumpActivity(fragment, ZFileQWActivity.class, map);
        }else {
            throw new IllegalArgumentException(ERROR_MSG);
        }
    }

    private void startByWechat(Object fragmentOrActivity) {
        ArrayMap<String, Object> map = getMap();
        if(fragmentOrActivity instanceof Activity) {
            Activity activity = (Activity) fragmentOrActivity;
            map.put(ZFileContent.QW_FILE_TYPE_KEY, ZFileConfiguration.WECHAT);
            ZFileContent.jumpActivity(activity, ZFileQWActivity.class, map);
        }else if(fragmentOrActivity instanceof Fragment) {
            Fragment fragment = (Fragment) fragmentOrActivity;
            map.put(ZFileContent.QW_FILE_TYPE_KEY, ZFileConfiguration.WECHAT);
            ZFileContent.jumpActivity(fragment, ZFileQWActivity.class, map);
        }else {
            throw new IllegalArgumentException(ERROR_MSG);
        }
    }

    private void startByFileManager(Object fragmentOrActivity, String path) {
        String newPath = "";
        if (TextUtils.isEmpty(path)) {
            newPath = ZFileContent.getSD_ROOT();
        }else {
            newPath = path;
        }
        if (!new File(newPath).exists()) {
            throw new NullPointerException(String.format("%s 路径不存在", newPath));
        }
        ArrayMap<String, Object> map = getMap();
        if(fragmentOrActivity instanceof Activity) {
            Activity activity = (Activity) fragmentOrActivity;
            map.put(ZFileContent.FILE_START_PATH_KEY, path);
            ZFileContent.jumpActivity(activity, ZFileListActivity.class, map);
        }else if(fragmentOrActivity instanceof Fragment) {
            Fragment fragment = (Fragment) fragmentOrActivity;
            map.put(ZFileContent.FILE_START_PATH_KEY, path);
            ZFileContent.jumpActivity(fragment, ZFileListActivity.class, map);
        }else {
            throw new IllegalArgumentException(ERROR_MSG);
        }
    }

    private ArrayMap<String, Object> getMap() {
        return new ArrayMap<>();
    }

    public static ZFileManageHelp getInstance() {
        return Builder.MANAGER;
    }

    public static final class Builder {
        
        private static final ZFileManageHelp MANAGER;
        
        public static final ZFileManageHelp.Builder INSTANCE;

        public static final ZFileManageHelp getMANAGER() {
            return MANAGER;
        }

        static {
            ZFileManageHelp.Builder builder = new ZFileManageHelp.Builder();
            INSTANCE = builder;
            MANAGER = new ZFileManageHelp();
        }
    }
}
