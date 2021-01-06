package com.kathline.library.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.kathline.library.R;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.util.ZFileOtherUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class ZFileAudioPlayDialog extends ZFileManageDialog implements SeekBar.OnSeekBarChangeListener, Runnable {

    private static final int UNIT = -1;
    private static final int PLAY = 0;
    private static final int PAUSE = 1;

    private int playerState = UNIT;

    private AudioHandler audioHandler = null;
    private MediaPlayer mediaPlayer = null;

    private long beginTime = 0;
    private long falgTime = 0;
    private long pauseTime = 0;
    private ImageView dialogZfileAudioPlay;
    private ImageView dialogAudioPause;
    private TextView dialogZfileAudioName;
    private AppCompatSeekBar dialogZfileAudioBar;
    private Chronometer dialogZfileAudioNowTime;
    private TextView dialogZfileAudioCountTime;

    public static ZFileAudioPlayDialog newInstance(String filePath) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        ZFileAudioPlayDialog fragment = new ZFileAudioPlayDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        ZFileContent.setNeedWH(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser && mediaPlayer != null) {
            mediaPlayer.seekTo(progress);
            falgTime = SystemClock.elapsedRealtime();
            beginTime = falgTime - seekBar.getProgress();
            dialogZfileAudioNowTime.setBase(beginTime);
            dialogZfileAudioNowTime.start();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public int getContentView() {
        return R.layout.dialog_zfile_audio_play;
    }

    @NonNull
    @Override
    public Dialog createDialog(@Nullable Bundle bundle) {
        Dialog dialog = new Dialog(getContext(), R.style.ZFile_Common_Dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        return dialog;
    }

    @Override
    public void init(@Nullable Bundle bundle) {
        initView();
        audioHandler = new AudioHandler(this);
        initPlayer();
        dialogZfileAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerState == PAUSE) {
                    startPlay();

                    falgTime = SystemClock.elapsedRealtime();
                    beginTime = falgTime - dialogZfileAudioBar.getProgress();
                    dialogZfileAudioNowTime.setBase(beginTime);
                    dialogZfileAudioNowTime.start();
                }else {
                    initPlayer();
                }
            }
        });
        dialogAudioPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playerState = PAUSE;

                    dialogZfileAudioNowTime.stop();
                    pauseTime = SystemClock.elapsedRealtime();

                    dialogZfileAudioPlay.setVisibility(View.VISIBLE);
                    dialogAudioPause.setVisibility(View.GONE);
                    dialogZfileAudioBar.setEnabled(false);
                }
            }
        });
        dialogZfileAudioBar.setOnSeekBarChangeListener(this);
        String filePath = getArguments().getString("filePath");
        if (filePath != null) {
            dialogZfileAudioName.setText(filePath.substring(filePath.lastIndexOf("/") + 1));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            playerState = UNIT;
            if(audioHandler != null) {
                audioHandler.removeMessages(0);
                audioHandler.removeCallbacks(this);
                audioHandler.removeCallbacksAndMessages(null);
                audioHandler = null;
            }
        }
    }

    @Override
    public void run() {
        if(mediaPlayer != null) {
            if(audioHandler != null) {
                audioHandler.sendEmptyMessage(0);
                audioHandler.postDelayed(this, 100);
            }
        }
    }

    private void initView() {
        dialogZfileAudioPlay = (ImageView) view.findViewById(R.id.dialog_zfile_audio_play);
        dialogAudioPause = (ImageView) view.findViewById(R.id.dialog_audio_pause);
        dialogZfileAudioName = (TextView) view.findViewById(R.id.dialog_zfile_audio_name);
        dialogZfileAudioBar = (AppCompatSeekBar) view.findViewById(R.id.dialog_zfile_audio_bar);
        dialogZfileAudioNowTime = (Chronometer) view.findViewById(R.id.dialog_zfile_audio_nowTime);
        dialogZfileAudioCountTime = (TextView) view.findViewById(R.id.dialog_zfile_audio_countTime);
    }

    private void initPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            String filePath = getArguments().getString("filePath");
            if(TextUtils.isEmpty(filePath)) {
                ZFileContent.toast(getContext(), "播放文件为空");
                return;
            }
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    dialogZfileAudioBar.setMax(mp.getDuration());
                    audioHandler.post(ZFileAudioPlayDialog.this);
                    dialogZfileAudioCountTime.setText(ZFileOtherUtil.secToTime(mp.getDuration() / 1000));
                    // 设置运动时间
                    falgTime = SystemClock.elapsedRealtime();
                    pauseTime = 0;
                    dialogZfileAudioNowTime.setBase(falgTime);
                    dialogZfileAudioNowTime.start();

                    startPlay();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay();
                    dialogZfileAudioBar.setProgress(0);

                    dialogZfileAudioNowTime.setBase(SystemClock.elapsedRealtime());
                    dialogZfileAudioNowTime.start();
                    dialogZfileAudioNowTime.stop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 开始播放
    private void startPlay() {
        if(mediaPlayer != null) {
            mediaPlayer.start();
            playerState = PLAY;
            dialogZfileAudioPlay.setVisibility(View.GONE);
            dialogAudioPause.setVisibility(View.VISIBLE);
            dialogZfileAudioBar.setEnabled(true);
        }
    }

    // 停止播放
    private void stopPlay() {
        dialogZfileAudioPlay.setVisibility(View.VISIBLE);
        dialogAudioPause.setVisibility(View.GONE);
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            playerState = UNIT;
            dialogZfileAudioBar.setEnabled(false);
        }
    }

    class AudioHandler extends Handler {

        private WeakReference<ZFileAudioPlayDialog> week;

        public AudioHandler(ZFileAudioPlayDialog dialog) {
            week = new WeakReference<ZFileAudioPlayDialog>(dialog);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (week.get() != null) {
                week.get().dialogZfileAudioBar.setProgress(week.get().mediaPlayer.getCurrentPosition());
            }
        }
    }
}
