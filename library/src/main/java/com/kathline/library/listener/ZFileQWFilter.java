package com.kathline.library.listener;

import androidx.annotation.NonNull;

import com.kathline.library.content.ZFileContent;

import java.io.File;
import java.io.FileFilter;

public final class ZFileQWFilter implements FileFilter {
    private String[] filterArray;
    private boolean isOther;

    public boolean accept(@NonNull File file) {
        if (this.isOther) {
            String name = file.getName();
            if (this.acceptOther(name)) {
                return true;
            }
        } else {
            for(int i = 0; i < filterArray.length; ++i) {
                String element = filterArray[i];
                String name = file.getName();
                if (ZFileContent.accept(name, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    private final boolean acceptOther(String name) {
        boolean isPNG = ZFileContent.accept(name, ZFileContent.PNG);
        boolean isJPG = ZFileContent.accept(name, ZFileContent.JPG);
        boolean isJPEG = ZFileContent.accept(name, ZFileContent.JPEG);
        boolean isGIF = ZFileContent.accept(name, ZFileContent.GIF);
        boolean isMP4 = ZFileContent.accept(name, ZFileContent.MP4);
        boolean is3GP = ZFileContent.accept(name, ZFileContent._3GP);
        boolean isTXT = ZFileContent.accept(name, ZFileContent.TXT);
        boolean isXML = ZFileContent.accept(name, ZFileContent.XML);
        boolean isJSON = ZFileContent.accept(name, ZFileContent.JSON);
        boolean isDOC = ZFileContent.accept(name, ZFileContent.DOC);
        boolean isXLS = ZFileContent.accept(name, ZFileContent.XLS);
        boolean isPPT = ZFileContent.accept(name, ZFileContent.PPT);
        boolean isPDF = ZFileContent.accept(name, ZFileContent.PDF);
        return !isPNG && !isJPG && !isJPEG && !isGIF && !isMP4 && !is3GP && !isTXT && !isXML && !isJSON && !isDOC && !isXLS && !isPPT && !isPDF;
    }

    public ZFileQWFilter(@NonNull String[] filterArray, boolean isOther) {
        this.filterArray = filterArray;
        this.isOther = isOther;
    }
}
