package com.kathline.library.util;

import com.kathline.library.common.ZFileType;
import com.kathline.library.common.ZFileTypeManage;
import com.kathline.library.content.ZFileContent;

import java.io.File;

public final class ZFileHelp {

    public static final ZFileHelp INSTANCE;

    public static final String getFileSize(String filePath) {
        return ZFileUtil.getFileSize(ZFileContent.toFile(filePath).length());
    }

    public static final ZFileType getFileType(String filePath) {
        return ZFileTypeManage.getTypeManager().getFileType(filePath);
    }

    public static final String getFileTypeBySuffix(String filePath) {
        return ZFileContent.getFileType(filePath);
    }


    public static final String getFormatFileDate(File file) {
        return ZFileOtherUtil.getFormatFileDate(file.lastModified());
    }

    private ZFileHelp() {
    }

    static {
        ZFileHelp zFileHelp = new ZFileHelp();
        INSTANCE = zFileHelp;
    }
}
