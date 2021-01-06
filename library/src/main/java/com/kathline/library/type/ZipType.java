package com.kathline.library.type;

import android.view.View;
import android.widget.ImageView;

import com.kathline.library.R;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;

/**
 * zip压缩包文件
 */
public class ZipType extends ZFileType {
    public void openFile(String filePath, View view) {
        ZFileContent.getZFileHelp().getFileOpenListener().openZIP(filePath, view);
    }

    public void loadingFile(String filePath, ImageView pic) {
        pic.setImageResource(this.getRes(ZFileContent.getZFileConfig().getResources().getZipRes(), R.drawable.ic_zfile_zip));
    }
}
