package com.kathline.library.async;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.kathline.library.Function;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;

public class ZFileThread {

    private SoftReference<Context> context;
    private Function block;
    private ZFileHandler handler;

    public ZFileThread(Context context, Function block) {
        this.context = new SoftReference<Context>(context);
        this.block = block;
        handler = new ZFileHandler(this);
    }

    public void start(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ZFileBean> list = ZFileContent.getZFileHelp().getFileLoadListener().getFileList(context.get(), filePath);
                Message obtain = Message.obtain();
                obtain.what = 10;
                obtain.obj = list;
                handler.sendMessage(obtain);
            }
        }).start();
    }

    static class ZFileHandler extends Handler {

        private WeakReference<ZFileThread> weakReference;

        public ZFileHandler(ZFileThread weakReference) {
            this.weakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            List<ZFileBean> list = (List<ZFileBean>) msg.obj;
            weakReference.get().block.invoke(list);
        }
    }
}
