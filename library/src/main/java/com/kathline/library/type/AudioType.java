package com.kathline.library.type;

import android.view.View;
import android.widget.ImageView;

import com.kathline.library.R;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;

/**
 * 音频文件
 */
public class AudioType extends ZFileType {
    public void openFile(String filePath, View view) {
        ZFileContent.getZFileHelp().getFileOpenListener().openAudio(filePath, view);
    }

    public void loadingFile(String filePath, ImageView pic) {
        int resId = ZFileContent.getZFileConfig().getResources().getAudioRes();
        pic.setImageResource(this.getRes(ZFileContent.getZFileConfig().getResources().getAudioRes(), R.drawable.ic_zfile_audio));
    }
}
