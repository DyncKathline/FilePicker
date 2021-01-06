package com.kathline.library.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class ZFileFragment extends Fragment {
    private boolean isFirstLoad = true;
    public View view;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(this.getContentView(), container, false);
        }

        return view;
    }

    public abstract int getContentView();

    public abstract void initAll();

    public void onResume() {
        super.onResume();
        if (this.isFirstLoad) {
            this.initAll();
            this.isFirstLoad = false;
        }

    }
}
