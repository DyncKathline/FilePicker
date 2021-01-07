package com.kathline.library.common;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class ZFileActivity extends AppCompatActivity {

    protected Context context;
    protected AppCompatActivity activity;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getContentView());
        context = this;
        activity = this;
        this.init(savedInstanceState);
    }

    public abstract int getContentView();

    public abstract void init(@Nullable Bundle var1);
}
