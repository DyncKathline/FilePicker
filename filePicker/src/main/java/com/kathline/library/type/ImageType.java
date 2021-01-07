package com.kathline.library.type;

import android.view.View;
import android.widget.ImageView;

import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;

/**
 * 图片文件
 */
public class ImageType extends ZFileType {
    public void openFile(String filePath, View view) {
        ZFileContent.getZFileHelp().getFileOpenListener().openImage(filePath, view);
    }

    public void loadingFile(String filePath, ImageView pic) {
        ZFileContent.getZFileHelp().getImageLoadListener().loadImage(pic, ZFileContent.toFile(filePath));
    }
}
