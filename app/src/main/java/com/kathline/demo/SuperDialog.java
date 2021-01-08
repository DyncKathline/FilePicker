package com.kathline.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据已经获取到了，具体怎么操作就交给你了！
 */
public class SuperDialog extends DialogFragment {

    private ImageView superCacelPic;
    private TextView zfileSelectFolderTitle;
    private ImageView superDownPic;
    private RecyclerView superRecyclerView;
    private View view;

    private SuperAdapter superAdapter = null;

    public interface OnListener {
        void onInit();
    }

    private OnListener onListener;

    public void setOnListener(OnListener listener) {
        onListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_super, container, false);
        initView();
        return view;
    }

    private void initView() {
        superCacelPic = (ImageView) view.findViewById(R.id.super_cacelPic);
        zfileSelectFolderTitle = (TextView) view.findViewById(R.id.zfile_select_folder_title);
        superDownPic = (ImageView) view.findViewById(R.id.super_downPic);
        superRecyclerView = (RecyclerView) view.findViewById(R.id.super_recyclerView);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.Zfile_Select_Folder_Dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
        }
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("ZFileManager", "数据已经获取到了，具体怎么操作就交给你了！");
        superDownPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        superCacelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        superAdapter = new SuperAdapter(new ArrayList<ZFileBean>());
        superRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        superRecyclerView.setAdapter(superAdapter);

        if(onListener != null) {
            onListener.onInit();
        }
    }

    public void setList(List<ZFileBean> list) {
        superAdapter.setDatas(list);
    }

    @Override
    public void onStart() {
        int[] display = ZFileContent.getDisplay(getContext());
       getDialog().getWindow().setLayout(display[0], display[1]);
        super.onStart();
    }
}