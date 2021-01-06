package com.kathline.fileoperatorjava.diy;

import com.kathline.fileoperatorjava.content.Content;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.listener.ZFileListener;
import com.kathline.library.util.ZFileHelp;

public class MyFileTypeListener extends ZFileListener.ZFileTypeListener {

    @Override
    public ZFileType getFileType(String filePath) {
        switch (ZFileHelp.getFileTypeBySuffix(filePath)) {
            case ZFileContent.TXT:
            case ZFileContent.XML:
            case ZFileContent.JSON:
                return new MyTxtType();
            case Content.APK:
                return new ApkType();
            default:
                return super.getFileType(filePath);
        }
    }
}