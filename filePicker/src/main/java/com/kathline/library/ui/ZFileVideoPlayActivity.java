package com.kathline.library.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.kathline.library.R;
import com.kathline.library.common.ZFileActivity;
import com.kathline.library.content.ZFileContent;

import java.io.File;

public class ZFileVideoPlayActivity extends ZFileActivity {
    private ZFileVideoPlayer videoPlayer;
    private ImageView videoImg;
    private ImageView videoPlayerButton;

    @Override
    public int getContentView() {
        return R.layout.activity_zfile_video_play;
    }

    @Override
    public void init(@Nullable Bundle var1) {
        ZFileContent.setStatusBarTransparent(this);
        final String videoPath = getIntent().getStringExtra("videoFilePath");
        initView();
        ZFileContent.getZFileHelp().getImageLoadListener().loadImage(videoImg, new File(videoPath));
        videoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.setVideoPath(videoPath);
                videoPlayer.play();
                v.setVisibility(View.GONE);
                videoImg.setVisibility(View.GONE);
            }
        });
        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoImg.setVisibility(View.VISIBLE);
                onBackPressed();
            }
        });
    }

    private void initView() {
        videoPlayer = (ZFileVideoPlayer) findViewById(R.id.video_player);
        videoImg = (ImageView) findViewById(R.id.video_img);
        videoPlayerButton = (ImageView) findViewById(R.id.videoPlayer_button);
    }
}
