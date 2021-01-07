package com.kathline.library.async;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.kathline.library.content.ZFileBean;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 更方便的去获取符合要求的数据
 */
public class ZFileAsync {

    private SoftReference<Context> context;
    private CallBack block;
    private ZFileAsyncHandler handler;

    public interface CallBack {
        void invoke(List<ZFileBean> list);
    }

    public ZFileAsync(Context context, CallBack block) {
        this.context = new SoftReference<>(context);
        this.block = block;
    }

    public void start(final String[] filterArray) {
        if (handler == null) {
            handler = new ZFileAsyncHandler(this);
        }
        onPreExecute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ZFileBean> list = doingWork(filterArray);
                Message message = Message.obtain();
                message.what = 20;
                message.obj = list;
                handler.sendMessage(message);
            }
        }).start();
    }

    protected Context getContext() {
        return context.get();
    }

    /**
     * 执行前调用 mainThread
     */
    protected void onPreExecute() {
    }

    /**
     * 获取数据
     * @param filterArray  规则
     */
    protected List<ZFileBean> doingWork(String[] filterArray) {
        return null;
    }

    private void onPostExecute(List<ZFileBean> list) {
        handler.removeMessages(20);
        handler.removeCallbacksAndMessages(null);
        handler = null;
        block.invoke(list);
        onPostExecute();
    }

    /**
     * 完成后调用 mainThread
     */
    protected void onPostExecute() {

    }

    static class ZFileAsyncHandler extends Handler {

        private WeakReference<ZFileAsync> weakReference;

        public ZFileAsyncHandler(ZFileAsync weakReference) {
            this.weakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            List<ZFileBean> list = (List<ZFileBean>) msg.obj;
            weakReference.get().onPostExecute(list);
        }
    }
}
