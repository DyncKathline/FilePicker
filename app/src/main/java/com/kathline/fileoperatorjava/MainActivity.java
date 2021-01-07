package com.kathline.fileoperatorjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
                ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                zFileConfig.setBoxStyle(ZFileConfiguration.STYLE2);
                zFileConfig.setMaxLength(6);
                zFileConfig.setMaxLengthStr("老铁最多6个文件");
                ZFileContent.getZFileHelp()
                        .setConfiguration(zFileConfig)
                        .start(this);
            }
        });
        mainFileMangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean hasPermission = hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasPermission) {
                        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        jump();
                    }
                } else {
                    jump();
                }
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
        initView();
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
        List<ZFileBean> list = ZFileContent.getZFileHelp().getSelectData(requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        for (ZFileBean bean : list) {
            sb.append(bean).append("\n\n");
        }
        mainResultTxt.setText(sb.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                jump();
            } else {
                Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasPermission(String permissions) {
        return ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String requestPermission) {
        ActivityCompat.requestPermissions(this, new String[]{requestPermission}, 100);
    }
}