package com.kathline.demo.content;

import android.app.Application;

import com.kathline.demo.diy.MyFileImageListener;
import com.kathline.demo.diy.MyFileTypeListener;
import com.kathline.library.content.ZFileContent;

public final class App extends Application {
    public void onCreate() {
        super.onCreate();
        ZFileContent.getZFileHelp().init(new MyFileImageListener()).setFileTypeListener(new MyFileTypeListener());
    }
}