package com.kathline.library.ui;

import android.content.Intent;

public interface ProxyListener {
    void onResult(int requestCode, int resultCode, Intent data);
}
