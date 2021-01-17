package com.kathline.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kathline.library.content.MimeType;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mainDefaultMangerBtn;
    private Button mainFileMangerBtn;
    private Button mainFragmentBtn;
    private Button mainJavaBtn;
    private TextView mainResultTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mainDefaultMangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ZFileConfiguration configuration = new ZFileConfiguration.Build()
                        .boxStyle(ZFileConfiguration.STYLE2)
                        .sortordBy(ZFileConfiguration.BY_DEFAULT)
                        .maxLength(2)
                        .maxLengthStr("亲，最多选2个！")
                        .useSAF(false)
                        .build();
                ZFileContent.getZFileHelp()
                        .setConfiguration(configuration)
                        .start(MainActivity.this);
            }
        });
        mainFileMangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
        mainFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FragmentSampleActivity.class));
            }
        }) ;
        mainJavaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JavaSampleActivity.class));
            }
        });
    }

    private void initView() {
        mainDefaultMangerBtn = (Button) findViewById(R.id.main_defaultMangerBtn);
        mainFileMangerBtn = (Button) findViewById(R.id.main_fileMangerBtn);
        mainFragmentBtn = (Button) findViewById(R.id.main_fragmentBtn);
        mainJavaBtn = (Button) findViewById(R.id.main_javaBtn);
        mainResultTxt = (TextView) findViewById(R.id.main_resultTxt);
    }

    private void jump() {
        startActivity(new Intent(this, SuperActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ZFileBean> list = ZFileContent.getZFileHelp().getSelectData(getBaseContext(), requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        for (ZFileBean bean : list) {
            sb.append(bean).append("\n\n");
        }
        mainResultTxt.setText(sb.toString());
    }

}
