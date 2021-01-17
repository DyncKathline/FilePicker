package com.kathline.library.async;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.util.ZFileLog;
import com.kathline.library.util.ZFileOtherUtil;
import com.kathline.library.util.ZFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的实现方式，你也可以自定义实现
 */
public class ZFileAsyncImpl extends ZFileAsync {

    public ZFileAsyncImpl(Context context, CallBack block) {
        super(context, block);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        ZFileLog.i("获取文件中...");
    }

    protected void onPostExecute() {
        ZFileLog.i("文件获取完成...");
    }

    @Nullable
    protected List<ZFileBean> doingWork(String[] filterArray) {
        return this.getLocalData(filterArray);
    }

    private final List<ZFileBean> getLocalData(String[] filterArray) {
        ArrayList<ZFileBean> list = new ArrayList<>();
        Cursor cursor = null;

        try {
            Uri fileUri = MediaStore.Files.getContentUri("external");
            StringBuilder sb = new StringBuilder();
            int length = filterArray.length;

            for (int i = 0; i < length; ++i) {
                String element = filterArray[i];
                if (element.equals(filterArray[filterArray.length - 1])) {
                    sb.append("_data").append(" LIKE '%." + element + '\'');
                } else {
                    sb.append("_data").append(" LIKE '%." + element + "' OR ");
                }
            }

            String selection = sb.toString();
            String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
            Context context = this.getContext();
            ContentResolver resolver = context != null ? context.getContentResolver() : null;
            cursor = resolver != null ? resolver.query(fileUri, null, selection, null, sortOrder) : null;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                    long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED));
                    String fileSize = ZFileUtil.getFileSize(size);
                    String lastModified = ZFileOtherUtil.getFormatFileDate(date * (long) 1000);
                    if (size > 0.0D) {
                        String name = path.substring(path.lastIndexOf("/") + 1, path.length());

                        list.add(new ZFileBean(name, true, path, lastModified, String.valueOf(date), fileSize, size));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            return list;
        }
    }
}

