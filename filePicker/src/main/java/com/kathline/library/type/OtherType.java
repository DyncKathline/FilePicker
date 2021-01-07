package com.kathline.library.type;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.kathline.library.R;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileContent;

/**
 * 其他类型的文件
 */
public class OtherType extends ZFileType {
    public void openFile(@NonNull String filePath, @NonNull View view) {
        ZFileContent.getZFileHelp().getFileOpenListener().openOther(filePath, view);
    }

    public void loadingFile(@NonNull String filePath, @NonNull ImageView pic) {
        pic.setImageResource(this.getRes(ZFileContent.getZFileConfig().getResources().getOtherRes(), R.drawable.ic_zfile_other));
    }
}
