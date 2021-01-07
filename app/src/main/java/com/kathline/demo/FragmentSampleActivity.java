package com.kathline.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FragmentSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.framgent_s_layout, new BlankFragment(), "BlankFragment")
                .commit();
    }
}
