package com.kathline.demo.diy;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.listener.ZFileListener;
import com.kathline.library.util.ZFileHelp;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MyQWFileListener extends ZFileListener.QWFileLoadListener {

    /**
     * 获取标题
     *
     * @return Array<String>
     */
    @Override
    public String[] getTitles() {
        return new String[]{"图片", "媒体", "文档", "其他"};
    }

    /**
     * 获取过滤规则
     *
     * @param fileType Int      文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
     */
    @Override
    public String[] getFilterArray(int fileType) {
        switch (fileType) {
            case ZFileContent.ZFILE_QW_PIC:
                return new String[]{ZFileContent.PNG, ZFileContent.JPG, ZFileContent.JPEG, "gif"};
            case ZFileContent.ZFILE_QW_MEDIA:
                return new String[]{ZFileContent.MP4, "3gp", "mp3"};
            case ZFileContent.ZFILE_QW_DOCUMENT:
                return new String[]{ZFileContent.PDF, ZFileContent.PPT, ZFileContent.DOC, ZFileContent.XLS};
            default:
                return new String[]{ZFileContent.TXT, ZFileContent.JSON, ZFileContent.XML, ZFileContent.ZIP, "rar"};
        }
    }

    /**
     * 获取 QQ 或 WeChat 文件路径
     *
     * @param qwType String         QQ 或 WeChat  see [ZFileConfiguration.QQ] [ZFileConfiguration.WECHAT]
     * @param fileType Int          文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
     * @return MutableList<String>  文件路径集合（因为QQ或WeChat保存的文件可能存在多个路径）
     */
    @Override
    public List<String> getQWFilePathArray(String qwType, int fileType) {
        ArrayList<String> listArray = new ArrayList<>();
        if (qwType.equals(ZFileConfiguration.QQ)) { // QQ
            switch (fileType) {
                case ZFileContent.ZFILE_QW_PIC: {
                    listArray.add("/storage/emulated/0/tencent/QQ_Images/");
                    listArray.add("/storage/emulated/0/Pictures/"); // QQ自定义路径1，仅做演示
                    listArray.add("/storage/emulated/0/DCIM/"); // QQ自定义路径2，仅做演示
                    listArray.add("/storage/emulated/0/Pictures/QQ/");
                }
                break;
                case ZFileContent.ZFILE_QW_MEDIA: {
                    listArray.add("/storage/emulated/0/Pictures/QQ/");
                }
                break;
                case ZFileContent.ZFILE_QW_DOCUMENT: {
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/");
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQ_business/");
                }
                break;
                default: {
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/");
                    listArray.add("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQ_business/");
                }
            }
        } else { // WeChat
            switch (fileType) {
                case ZFileContent.ZFILE_QW_PIC: {
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/");
                }
                break;
                case ZFileContent.ZFILE_QW_MEDIA: {
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/");
                }
                break;
                case ZFileContent.ZFILE_QW_DOCUMENT: {
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/Download/");
                }
                break;
                default: {
                    listArray.add("/storage/emulated/0/tencent/MicroMsg/Download/");
                }
            }
        }
        return listArray;
    }

    /**
     * 获取数据
     *
     * @param fileType Int                          文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
     * @param qwFilePathArray MutableList<String>   QQ 或 WeChat 文件路径集合
     * @param filterArray Array<String>             过滤规则
     */
    @Override
    public List<ZFileBean> getQWFileDatas(int fileType, List<String> qwFilePathArray, String[] filterArray) {
        ArrayList<File[]> pathListFile = new ArrayList<>();
        for (String path : qwFilePathArray) {
            File file = new File(path);
            if (file.exists()) {
                pathListFile.add(file.listFiles(new MyQWFilter(filterArray)));
            }
        }
        if (pathListFile.isEmpty()) return new ArrayList<>();
        ArrayList<ZFileBean> list = new ArrayList<>();
        for (File[] files : pathListFile) {
            for (File file : files) {
                if (!file.isHidden()) {
                    ZFileBean bean = new ZFileBean(
                            file.getName(),
                            file.isFile(),
                            file.getPath(),
                            ZFileHelp.getFormatFileDate(file),
                            file.lastModified()+"",
                            ZFileHelp.getFileSize(file.getPath()),
                            file.length()
                    );
                    list.add(bean);
                }
            }
        }
        if (!list.isEmpty()) {
            Collections.sort(list, new Comparator<ZFileBean>() {
                @Override
                public int compare(ZFileBean o1, ZFileBean o2) {
                    return o1.getOriginalDate().compareTo(o2.getOriginalDate());
                }
            });
        }
        return list;
    }


    class MyQWFilter implements FileFilter {

        private String[] filterArray;

        public MyQWFilter(String[] filterArray) {
            this.filterArray = filterArray;
        }

        @Override
        public boolean accept(File pathname) {
            for (String s : filterArray) {
                if (accept(pathname.getName(), s)) {
                    return true;
                }
            }
            return false;
        }

        private boolean accept(String source, String type) {
            return source.endsWith(type.toLowerCase(Locale.CHINA)) || source.endsWith(type.toUpperCase(Locale.CHINA));
        }
    }

}

