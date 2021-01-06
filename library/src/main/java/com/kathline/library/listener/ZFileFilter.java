package com.kathline.library.listener;

import androidx.annotation.Nullable;

import com.kathline.library.content.ZFileContent;

import java.io.File;
import java.io.FileFilter;

public final class ZFileFilter implements FileFilter {
    private String[] fileArray;
    private boolean isOnlyFolder;
    private boolean isOnlyFile;

    public boolean accept(File file) {
        if (this.isOnlyFolder) {// 只显示文件夹
            return file.isDirectory();
        } else if (this.isOnlyFile) {// 只显示文件
            return file.isFile();
        } else if (file.isDirectory()) {// 文件夹直接返回
            return true;
        } else {
            if (this.fileArray != null) {
                if (fileArray.length != 0) {
                    int length = fileArray.length;

                    for(int i = 0; i < length; ++i) {
                        String element = fileArray[i];
                        String name = file.getName();
                        if (ZFileContent.accept(name, element)) {
                            return true;
                        }
                    }
                    return false;
                }
            }

            return true;
        }
    }

    public ZFileFilter(@Nullable String[] fileArray, boolean isOnlyFolder, boolean isOnlyFile) {
        this.fileArray = fileArray;
        this.isOnlyFolder = isOnlyFolder;
        this.isOnlyFile = isOnlyFile;
    }
}
