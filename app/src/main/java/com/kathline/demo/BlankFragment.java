package com.kathline.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;

import java.util.List;

public class BlankFragment extends Fragment {

    private Button framgentStartBtn;
    private Button framgentWechatBtn;
    private TextView mainResultTxt;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);
        initView();
        return view;
    }

    private void initView() {
        framgentStartBtn = (Button) view.findViewById(R.id.framgent_startBtn);
        framgentWechatBtn = (Button) view.findViewById(R.id.framgent_wechatBtn);
        mainResultTxt = (TextView) view.findViewById(R.id.main_resultTxt);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        framgentStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZFileContent.getZFileHelp().start(this);
            }
        });
        framgentWechatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                zFileConfig.setFilePath(ZFileConfiguration.WECHAT);
                ZFileContent.getZFileHelp().setConfiguration(zFileConfig).start(this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ZFileBean> list = ZFileContent.getZFileHelp().getSelectData(requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        for (ZFileBean bean : list) {
            sb.append(bean).append("\n\n");
        }
        mainResultTxt.setText(sb.toString());
    }
}
