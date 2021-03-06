package com.kathline.library.type;

import android.view.View;
import android.widget.ImageView;

import com.kathline.library.R;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;

/**
 * word文件
 */
public class WordType extends ZFileType {
    public void openFile(String filePath, View view) {
        ZFileContent.getZFileHelp().getFileOpenListener().openDOC(filePath, view);
    }

    public void loadingFile(String filePath, ImageView pic) {
        pic.setImageResource(this.getRes(ZFileContent.getZFileConfig().getResources().getWordRes(), R.drawable.ic_zfile_word));
    }
}
