package com.kathline.library.listener;

import android.content.Context;
import android.text.TextUtils;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.util.ZFileOtherUtil;
import com.kathline.library.util.ZFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ZFileDefaultLoadListener implements ZFileListener.ZFileLoadListener {

    @Override
    public List<ZFileBean> getFileList(Context context, String filePath) {
        return getDefaultFileList(context, filePath);
    }

    private List<ZFileBean> getDefaultFileList(Context context, String filePath) {
        String path = "";
        if (TextUtils.isEmpty(filePath)) {
            path = ZFileContent.getSD_ROOT();
        } else {
            path = filePath;
        }
        ZFileConfiguration config = ZFileContent.getZFileConfig();
        ArrayList<ZFileBean> list = new ArrayList<>();
        File[] listFiles = new File(path).listFiles(new ZFileFilter(config.getFileFilterArray(), config.isOnlyFolder(), config.isOnlyFile()));
        if(listFiles == null) {
            return null;
        }
        for (File file : listFiles) {
            if (config.isShowHiddenFile()) {
                ZFileBean bean = new ZFileBean(
                        file.getName(),
                        file.isFile(),
                        file.getPath(),
                        !TextUtils.isEmpty(ZFileOtherUtil.getFormatFileDate(file.lastModified())) ? ZFileOtherUtil.getFormatFileDate(file.lastModified()) : "未知时间",
                        file.lastModified() + "",
                        ZFileUtil.getFileSize(file.length()),
                        file.length()
                );
                list.add(bean);
            }else {
                if(!file.isHidden()) {
                    ZFileBean bean = new ZFileBean(
                            file.getName(),
                            file.isFile(),
                            file.getPath(),
                            !TextUtils.isEmpty(ZFileOtherUtil.getFormatFileDate(file.lastModified())) ? ZFileOtherUtil.getFormatFileDate(file.lastModified()) : "未知时间",
                            file.lastModified() + "",
                            ZFileUtil.getFileSize(file.length()),
                            file.length()
                    );
                    list.add(bean);
                }
            }
        }

        // 排序相关
        if (config.getSortord() == ZFileConfiguration.ASC) {
            switch (config.getSortordBy()) {
                case ZFileConfiguration.BY_NAME:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return o1.getFileName().toLowerCase(Locale.CHINA).compareTo(o2.getFileName().toLowerCase(Locale.CHINA));
                        }
                    });
                    break;
                case ZFileConfiguration.BY_DATE:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return o1.getOriginalDate().compareTo(o2.getOriginalDate());
                        }
                    });
                    break;
                case ZFileConfiguration.BY_SIZE:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return (int) (o1.getOriginalSize() - o2.getOriginalSize());
                        }
                    });
                    break;
            }
        } else {
            switch (config.getSortordBy()) {
                case ZFileConfiguration.BY_NAME:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return o2.getFileName().toLowerCase(Locale.CHINA).compareTo(o1.getFileName().toLowerCase(Locale.CHINA));
                        }
                    });
                    break;
                case ZFileConfiguration.BY_DATE:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return o2.getOriginalDate().compareTo(o1.getOriginalDate());
                        }
                    });
                    break;
                case ZFileConfiguration.BY_SIZE:
                    Collections.sort(list, new Comparator<ZFileBean>() {
                        @Override
                        public int compare(ZFileBean o1, ZFileBean o2) {
                            return (int) (o2.getOriginalSize() - o1.getOriginalSize());
                        }
                    });
                    break;
            }
        }
        return list;
    }
}
