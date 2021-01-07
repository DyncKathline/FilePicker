package com.kathline.library.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kathline.library.util.ZFileLog;

/**
 * 该处的实现请查看 JZVideoPlayer ，VideoView
 * 只是单纯的实现一个视频播放功能，如需手势功能，进度条拖拽可直接使用 JZVideoPlayer
 */
public class ZFileVideoPlayer extends TextureView implements TextureView.SurfaceTextureListener {

    private MediaPlayer player;
    private String videoPath;
    private int videoWidth;
    private int videoHeight;
    private float leftVolume;
    private float rightVolume;
    private final int IS_INIT = 0;
    private final int IS_PLAYING = 1;
    private final int IS_PAUSE = 2;
    private int playState = IS_INIT;
    private int size_type = CENTER_MODE;
    /**
     * 中心裁剪模式
     */
    public static final int CENTER_CROP_MODE = 0x10001;
    /**
     * 中心填充模式
     */
    public static final int CENTER_MODE = 0x10002;

    /**
     * 父容器多大，视频多大，强制填充显示
     */
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 0x1001;
    /**
     * 原视频大小显示
     */
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 0x1002;
    /**
     * 剪切部分视频大小显示
     */
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 0x1003;

    private VideoPlayListener videoPlayListener;

    public interface VideoPlayListener {
        void invoke(MediaPlayer mp);
    }

    public void setVideoPlayListener(VideoPlayListener listener) {
        videoPlayListener = listener;
    }

    public ZFileVideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public ZFileVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public ZFileVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSurfaceTextureListener(this);
        ZFileLog.i("初始化....");
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        if (player == null) {
            player = new MediaPlayer();

            /** 当装载流媒体完毕的时候回调 */
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    ZFileLog.i("媒体装载完成");
                    setVolume(1, 1);
                    if (videoPlayListener != null) {
                        videoPlayListener.invoke(player);
                    }
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    ZFileLog.i(String.format("缓存中：%d", percent));
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ZFileLog.i("播放完成");
                    play();
                }
            });
            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    videoWidth = width;
                    videoHeight = height;
                    updateTextureViewSize();
                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    ZFileLog.e("播放失败");
                    return false;
                }
            });
            Surface s = new Surface(surface);
            // 将surface 与播放器进行绑定
            player.setSurface(s);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (player != null) {
            player.pause();
            player.stop();
            player.release();
            ZFileLog.i("播放器被销毁...");
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    private void setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        if (player != null) {
            player.setVolume(leftVolume, rightVolume);
        }
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        } else {
            return false;
        }
    }

    public boolean isPause() {
        return playState == IS_PAUSE;
    }

    /**
     * 播放或暂停后播放
     */
    public void play() {
        if (player == null) {
            return;
        }
        if (videoPath.isEmpty()) {
            ZFileLog.i("视频播放地址不能为空");
            return;
        }
        if (isPlaying()) {
            ZFileLog.i("视频正在播放...");
            return;
        }
        try {
            if (isPause()) {
                if (player != null) {
                    player.start();
                }
            } else {
                if (player != null) {
                    player.reset();
                    player.setDataSource(videoPath);
                    player.prepare();
                    player.start();
                }
            }
            playState = IS_PLAYING;
        } catch (Exception e) {
            e.printStackTrace();
            ZFileLog.e("播放失败！视频路径为：$videoPath");
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (player == null) return;
        if (isPlaying()) {
            if (player != null) {
                player.pause();
            }
            playState = IS_PAUSE;
        }
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        requestLayout();
    }

    private void updateTextureViewSize() {
        switch (size_type) {
            case CENTER_MODE:
                updateTextureViewSizeCenter(0);
                break;
            case CENTER_CROP_MODE:
                updateTextureViewSizeCenterCrop();
                break;
            default:
                throw new IllegalArgumentException("参数错误");
        }
    }

    // 剪裁部分视频内容并全屏显示
    private void updateTextureViewSizeCenterCrop() {
        float sx = getWidth()*1f / videoWidth;
        float sy = getHeight()*1f / videoHeight;
        Matrix matrix = new Matrix();
        float maxScale = Math.max(sx, sy);
        // 视频区移动到View区,使两者中心点重合.
        matrix.preTranslate(((getWidth() - videoWidth*1f) / 2), ((getHeight() - videoHeight*1f) / 2));
        // 默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth*1f / getWidth(), videoHeight*1f / getHeight());
        // 等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等.
        matrix.postScale(maxScale, maxScale, (getWidth()*1f / 2), (getHeight()*1f / 2));
        setTransform(matrix);
        postInvalidate();
    }

    // 居中显示
    private void updateTextureViewSizeCenter(float rotation) {
        if (rotation == 90f || rotation == 270f) { // 宽高与之前相反

        }
        float sx = getWidth()*1f / videoWidth;
        float sy = getHeight()*1f / videoHeight;
        Matrix matrix = new Matrix();
        // 把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate(((getWidth() - videoWidth*1f) / 2), ((getHeight() - videoHeight*1f) / 2));
        // 默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth*1f / getWidth(), videoHeight*1f / getHeight());
        // 等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy) {
            matrix.postScale(sy, sy, (getWidth()*1f / 2), (getHeight()*1f / 2));
        } else {
            matrix.postScale(sx, sx, (getWidth()*1f / 2), (getHeight()*1f / 2));
        }
        setTransform(matrix);
        postInvalidate();
    }

}
