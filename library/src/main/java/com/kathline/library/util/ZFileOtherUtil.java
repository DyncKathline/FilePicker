package com.kathline.library.util;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.kathline.library.content.ZFileInfoBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZFileOtherUtil {
    /**
     * 时间戳格式化
     */
    public static String getFormatFileDate(long seconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return dateFormat.format(new Date(seconds));
    }


    /** int类型转时分秒格式 */
    public static String secToTime(int time) {
        String timeStr = null;
        if (time <= 0) {
            return "00:00";
        } else {
            int minute = time / 60;
            int second;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                int hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }

                minute %= 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }

            return timeStr;
        }
    }

    public static String unitFormat(int i) {
        String format;
        if (0 <= i) {
            if (9 >= i) {
                format = "" + '0' + i;
                return format;
            }
        }

        format = String.valueOf(i);
        return format;
    }

    /**
     * 获取媒体信息
     */
    public static ZFileInfoBean getMultimediaInfo(@NonNull String path, boolean isVideo) {
        int duration = -1;
        String width = "0";
        String height = "0";
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            if (isVideo) {
                width = mediaPlayer.getVideoWidth()+"";
                height = mediaPlayer.getVideoHeight()+"";
            }
            duration = mediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            return new ZFileInfoBean(secToTime(duration / 1000), width, height);
        }
    }

    /**
     * 根据路径获取图片的宽、高信息
     */
    public static Integer[] getImageWH(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        ZFileLog.i("width---" + options.outWidth + " height---" + options.outHeight);
        return new Integer[]{options.outWidth, options.outHeight};
    }
}
