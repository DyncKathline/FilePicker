package com.kathline.fileoperatorjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kathline.fileoperatorjava.diy.MyQWFileListener;
import com.kathline.library.async.ZFileAsync;
import com.kathline.library.async.ZFileAsyncImpl;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;

import java.util.ArrayList;
import java.util.List;

public class SuperActivity extends AppCompatActivity {

    private ProgressDialog dialog = null;
    private TextView superPicTxt;
    private TextView superVideoTxt;
    private TextView superAudioTxt;
    private TextView superFileTxt;
    private TextView superWpsTxt;
    private TextView superApkTxt;
    private TextView superQqTxt;
    private TextView superWechatTxt;
    private TextView superOtherTxt;
    private RadioGroup superGroup;
    private RadioButton superDefaultRadio;
    private RadioButton superDiyRadio;
    private TextView superInnerTxt;
    private TextView superResultTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);
        initView();
        dialog = new ProgressDialog(this);

        dialog.setMessage("获取中，请稍后...");
        dialog.setCancelable(false);

        superPicTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{ZFileContent.PNG, ZFileContent.JPEG, ZFileContent.JPG, ZFileContent.GIF});
            }
        });

        superVideoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{ZFileContent.MP4, ZFileContent._3GP});
            }
        });

        superAudioTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{ZFileContent.MP3, ZFileContent.AAC, ZFileContent.WAV});
            }
        });

        superFileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{ZFileContent.TXT, ZFileContent.JSON, ZFileContent.XML});
            }
        });

        superWpsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{ZFileContent.DOC, ZFileContent.XLS, ZFileContent.PPT, ZFileContent.PDF});
            }
        });

        superApkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new String[]{"apk"});
            }
        });

        superQqTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQW(ZFileConfiguration.QQ);
            }
        });

        superWechatTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQW(ZFileConfiguration.WECHAT);
            }
        });

        superOtherTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SuperActivity.this, "我只是一个占位格，好看的", Toast.LENGTH_SHORT).show();
            }
        });

        superGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.super_diyRadio: {
                        ZFileContent.getZFileHelp().setQWFileLoadListener(new MyQWFileListener());
                    }
                    default: {
                        ZFileContent.getZFileHelp().setQWFileLoadListener(null);
                    }
                }
            }
        });

        superInnerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                    zFileConfig.setNeedLongClick(false);
                    zFileConfig.setOnlyFolder(true);
                    zFileConfig.setSortordBy(ZFileConfiguration.BY_NAME);
                    zFileConfig.setSortord(ZFileConfiguration.ASC);
                    ZFileContent.getZFileHelp().setConfiguration(zFileConfig).start(SuperActivity.this);
            }
        });
    }

    private void initView() {
        superPicTxt = (TextView) findViewById(R.id.super_picTxt);
        superVideoTxt = (TextView) findViewById(R.id.super_videoTxt);
        superAudioTxt = (TextView) findViewById(R.id.super_audioTxt);
        superFileTxt = (TextView) findViewById(R.id.super_fileTxt);
        superWpsTxt = (TextView) findViewById(R.id.super_wpsTxt);
        superApkTxt = (TextView) findViewById(R.id.super_apkTxt);
        superQqTxt = (TextView) findViewById(R.id.super_qqTxt);
        superWechatTxt = (TextView) findViewById(R.id.super_wechatTxt);
        superOtherTxt = (TextView) findViewById(R.id.super_otherTxt);
        superGroup = (RadioGroup) findViewById(R.id.super_group);
        superDefaultRadio = (RadioButton) findViewById(R.id.super_defaultRadio);
        superDiyRadio = (RadioButton) findViewById(R.id.super_diyRadio);
        superInnerTxt = (TextView) findViewById(R.id.super_innerTxt);
        superResultTxt = (TextView) findViewById(R.id.super_resultTxt);
    }

    private void toQW(String path) {
        Log.e("ZFileManager", "请注意：QQ、微信目前只能获取用户手动保存到手机里面的文件，" +
                "且保存文件到手机的目录用户没有修改");
        Log.i("ZFileManager", "参考自腾讯自己的\"腾讯文件\"App，能力有限，部分文件无法获取");
        jump(path);
    }

    private void jump(String path) {
        ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
        zFileConfig.setBoxStyle(ZFileConfiguration.STYLE2);
        zFileConfig.setFilePath(path);
        ZFileContent.getZFileHelp().setConfiguration(zFileConfig).start(this);
    }

    private void showDialog(String[] filterArray) {
        if(dialog != null) {
            dialog.show();
        }
        new ZFileAsyncImpl(this, new ZFileAsync.CallBack() {
            @Override
            public void invoke(List<ZFileBean> list) {
                if(dialog != null) {
                    dialog.dismiss();
                }
                if (list == null || list.isEmpty()) {
                    Toast.makeText(SuperActivity.this,"暂无数据", Toast.LENGTH_SHORT).show();
                } else {
                    if (list.size() > 100){
                        Log.e("ZFileManager", "这里考虑到传值大小限制，截取前100条数据");
                        SuperDialog.newInstance(changeList(list))
                                .show(getSupportFragmentManager(), "SuperDialog");
                    } else{
                        SuperDialog.newInstance((ArrayList<ZFileBean>) list)
                                .show(getSupportFragmentManager(), "SuperDialog");
                    }
                }
            }
        }).start(filterArray);
    }

    private ArrayList<ZFileBean> changeList(List<ZFileBean> oldList) {
        ArrayList<ZFileBean> list = new ArrayList<>();
        int index = 1;
        for (ZFileBean bean : oldList) {
            if (index >= 100) {
                break;
            }
            list.add(bean);
            index++;
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ZFileBean> list = ZFileContent.getZFileHelp().getSelectData(requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        for (ZFileBean bean : list) {
            sb.append(bean).append("\n\n");
        }
        superResultTxt.setText(sb.toString());
    }

    private void reset() {
        // 这里重置，防止该页面销毁后其他演示页面无法正常获取数据！
        ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
        zFileConfig.setNeedLongClick(true);
        zFileConfig.setOnlyFolder(false);
        zFileConfig.setSortordBy(ZFileConfiguration.BY_DEFAULT);
        zFileConfig.setSortord(ZFileConfiguration.ASC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZFileContent.getZFileHelp().setQWFileLoadListener(null);
        reset();
    }
}
