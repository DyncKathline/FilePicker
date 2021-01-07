package com.kathline.demo.diy;

import android.widget.ImageView;

import com.kathline.demo.R;
import com.kathline.library.type.TxtType;

/**
 * 改变 txt 原本显示的图标
 */
public class MyTxtType extends TxtType {

    @Override
    public void loadingFile(String filePath, ImageView pic) {
        pic.setImageResource(R.drawable.ic_my_txt);
    }

}