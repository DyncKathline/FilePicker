package com.kathline.library.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kathline.library.Function;
import com.kathline.library.R;
import com.kathline.library.common.ZFileAdapter;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.common.ZFileViewHolder;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.util.ZFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZFileSelectFolderDialog extends ZFileManageDialog {

    private String tipStr = "";
    private String filePath = "";
    private boolean isOnlyFolder = false;
    private boolean isOnlyFile = false;
    private ZFileAdapter<ZFileBean> folderAdapter = null;
    private ArrayList<String> backList = new ArrayList<>();
    private ImageView zfileSelectFolderClosePic;
    private TextView zfileSelectFolderTitle;
    private ImageView zfileSelectFolderDownPic;
    private RecyclerView zfileSelectFolderRecyclerView;

    private void initView() {
        zfileSelectFolderClosePic = (ImageView) view.findViewById(R.id.zfile_select_folder_closePic);
        zfileSelectFolderTitle = (TextView) view.findViewById(R.id.zfile_select_folder_title);
        zfileSelectFolderDownPic = (ImageView) view.findViewById(R.id.zfile_select_folder_downPic);
        zfileSelectFolderRecyclerView = (RecyclerView) view.findViewById(R.id.zfile_select_folder_recyclerView);
    }

    public interface SelectFolderListener {
        void invoke(String folder);
    }

    private SelectFolderListener selectFolder;

    public void setSelectFolderListener(SelectFolderListener listener) {
        selectFolder = listener;
    }

    public static ZFileSelectFolderDialog newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        ZFileSelectFolderDialog fragment = new ZFileSelectFolderDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_zfile_select_folder;
    }

    @NonNull
    @Override
    public Dialog createDialog(@Nullable Bundle bundle) {
        Dialog dialog = new Dialog(getContext(), R.style.ZFile_Common_Dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
        }
        return dialog;
    }

    @Override
    public void init(@Nullable Bundle bundle) {
        initView();
        tipStr = getArguments().getString("type");
        if(TextUtils.isEmpty(tipStr)) {
            tipStr = ZFileConfiguration.COPY;
        }
        // 先保存之前用户配置的数据
        filePath = ZFileContent.getZFileConfig().getFilePath();
        isOnlyFile = ZFileContent.getZFileConfig().isOnlyFile();
        isOnlyFolder = ZFileContent.getZFileConfig().isOnlyFolder();
        zfileSelectFolderClosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        zfileSelectFolderDownPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectFolder != null) {
                    String filePath = ZFileContent.getZFileConfig().getFilePath();
                    if(TextUtils.isEmpty(filePath)) {
                        filePath = ZFileContent.getSD_ROOT();
                    }
                    selectFolder.invoke(filePath);
                    recoverData();
                    dismiss();
                }
            }
        });
        zfileSelectFolderTitle.setText(String.format("%s到根目录", tipStr));
        initRecyclerView();
    }

    private void initRecyclerView() {
        folderAdapter = new ZFileAdapter<ZFileBean>(getContext(), R.layout.item_zfile_list_folder) {
            @Override
            protected void bindView(ZFileViewHolder holder, ZFileBean item, int position) {
                holder.setText(R.id.item_zfile_list_folderNameTxt, item.getFileName());
                holder.setImageRes(R.id.item_zfile_list_folderPic, ZFileContent.getFolderRes());
                holder.setBgColor(R.id.item_zfile_list_folder_line, ZFileContent.getLineColor());
                holder.setVisibility(R.id.item_zfile_list_folder_line, position < getItemCount() - 1);
            }
        };
        folderAdapter.setOnClickListener(new ZFileAdapter.OnClickListener<ZFileBean>() {
            @Override
            public void onClick(View view, int position, ZFileBean data) {
                ZFileContent.getZFileConfig().setFilePath(data.getFilePath());
                backList.add(data.getFilePath());
                getData();
            }
        });
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) zfileSelectFolderRecyclerView.getLayoutParams();
        lp.setMargins(0, 0, 0, ZFileContent.getStatusBarHeight(getContext()));
        zfileSelectFolderRecyclerView.setLayoutParams(lp);
        zfileSelectFolderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        zfileSelectFolderRecyclerView.setAdapter(folderAdapter);
        ZFileContent.getZFileConfig().setOnlyFile(false);
        ZFileContent.getZFileConfig().setOnlyFolder(true);
        ZFileContent.getZFileConfig().setFilePath("");
        getData();
    }

    private void getData() {
        String filePath = ZFileContent.getZFileConfig().getFilePath();
        if(TextUtils.isEmpty(filePath) || filePath == ZFileContent.getSD_ROOT()) {
            zfileSelectFolderTitle.setText(String.format("%s到根目录", tipStr));
        }else {
            zfileSelectFolderTitle.setText(String.format("%s到%s", tipStr, new File(filePath).getName()));
        }
        ZFileUtil.getList(getContext(), new Function() {
            @Override
            public void invoke(List<ZFileBean> list) {
                if(list == null || list.isEmpty()) {
                    folderAdapter.clear();
                }else {
                    folderAdapter.setDatas(list);
                }
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        String path = getThisFilePath();
        if(TextUtils.isEmpty(path) || (path != null && path.equals(ZFileContent.getSD_ROOT()))) { // 根目录
            dismiss();
        }else { // 返回上一级
            backList.remove(backList.size() - 1);
            ZFileContent.getZFileConfig().setFilePath(getThisFilePath());
            getData();
        }
        return true;
    }

    private void recoverData() {
        // 恢复之前用户配置的数据
        ZFileContent.getZFileConfig().setFilePath(filePath);
        ZFileContent.getZFileConfig().setOnlyFile(isOnlyFile);
        ZFileContent.getZFileConfig().setOnlyFolder(isOnlyFolder);
    }

    @Override
    public void onStart() {
        int[] display = ZFileContent.getDisplay(getContext());
        getDialog().getWindow().setLayout(display[0], display[1]);
        super.onStart();
    }

    /**
     * 返回当前的路径
     */
    private String getThisFilePath() {
        if (backList.isEmpty()) {
            return null;
        } else {
            return backList.get(backList.size() - 1);
        }
    }
}
