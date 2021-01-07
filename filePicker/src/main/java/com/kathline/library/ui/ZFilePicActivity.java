package com.kathline.library.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.kathline.library.R;
import com.kathline.library.common.ZFileActivity;
import com.kathline.library.content.ZFileContent;

import java.io.File;

public class ZFilePicActivity extends ZFileActivity {

    private ImageView zfilePicShow;

    @Override
    public int getContentView() {
        return R.layout.activity_zfile_pic;
    }

    @Override
    public void init(@Nullable Bundle var1) {
        initView();
        ZFileContent.setStatusBarTransparent(this);
        String filePath = getIntent().getStringExtra("picFilePath");
        ZFileContent.getZFileHelp().getImageLoadListener().loadImage(zfilePicShow, new File(filePath));
        zfilePicShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        zfilePicShow = (ImageView) findViewById(R.id.zfile_pic_show);
    }
}
