package com.kathline.library.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kathline.library.R;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.common.ZFileType;
import com.kathline.library.common.ZFileTypeManage;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.content.ZFileInfoBean;
import com.kathline.library.type.AudioType;
import com.kathline.library.type.ImageType;
import com.kathline.library.type.VideoType;
import com.kathline.library.util.ZFileOtherUtil;

import java.lang.ref.WeakReference;

public class ZFileInfoDialog extends ZFileManageDialog implements Runnable {

    private TextView zfileDialogInfoFileName;
    private TextView zfileDialogInfoFileType;
    private TextView zfileDialogInfoFileSize;
    private TextView zfileDialogInfoFileDate;
    private TextView zfileDialogInfoFilePath;
    private CheckBox zfileDialogInfoMoreBox;
    private LinearLayout zfileDialogInfoMoreLayout;
    private LinearLayout zfileDialogInfoFileDurationLayout;
    private TextView zfileDialogInfoFileDuration;
    private LinearLayout zfileDialogInfoFileFBLLayout;
    private TextView zfileDialogInfoFileFBL;
    private TextView zfileDialogInfoFileOther;
    private Button zfileDialogInfoDown;

    private ZFileBean bean;
    private String filePath;
    private ZFileType fileType;
    private InfoHandler handler;
    private Thread thread;

    public static ZFileInfoDialog newInstance(ZFileBean bean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("fileBean", bean);
        ZFileInfoDialog fragment = new ZFileInfoDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_zfile_info;
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
        if(getArguments() != null) {
            bean = getArguments().getParcelable("fileBean");
            if(bean == null) {
                bean = new ZFileBean();
            }
        }
        initView();
        String filePath = bean.getFilePath();
        this.filePath = filePath;
        fileType = ZFileTypeManage.getTypeManager().getFileType(filePath);
        handler = new InfoHandler(this);
        thread = new Thread(this);
        thread.start();

        zfileDialogInfoFileName.setText(bean.getFileName());
        zfileDialogInfoFileType.setText(filePath.substring(filePath.lastIndexOf(".") + 1));
        zfileDialogInfoFileDate.setText(bean.getDate());
        zfileDialogInfoFileSize.setText(bean.getSize());
        zfileDialogInfoFilePath.setText(bean.getFilePath());

        zfileDialogInfoMoreBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zfileDialogInfoMoreLayout.setVisibility(zfileDialogInfoMoreBox.isChecked() ? View.VISIBLE : View.GONE);
            }
        });
        if(fileType instanceof ImageType) {
            zfileDialogInfoMoreBox.setVisibility(View.VISIBLE);
            zfileDialogInfoFileDurationLayout.setVisibility(View.GONE);
            zfileDialogInfoFileOther.setText("无");
            Integer[] wh = ZFileOtherUtil.getImageWH(filePath);
            zfileDialogInfoFileFBL.setText(String.format("%d * %d", wh[0], wh[1]));
        }else if(fileType instanceof AudioType) {
            zfileDialogInfoMoreBox.setVisibility(View.VISIBLE);
            zfileDialogInfoFileFBLLayout.setVisibility(View.GONE);
            zfileDialogInfoFileOther.setText("无");
        }else if(fileType instanceof VideoType) {
            zfileDialogInfoMoreBox.setVisibility(View.VISIBLE);
            zfileDialogInfoFileOther.setText("无");
        }else {
            zfileDialogInfoMoreBox.setVisibility(View.GONE);
            zfileDialogInfoMoreLayout.setVisibility(View.GONE);
        }
        zfileDialogInfoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ZFileContent.setNeedWH(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(handler != null) {
            handler.removeMessages(0);
            handler.removeCallbacks(this);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void run() {
        if(fileType instanceof AudioType) {
            return;
        }
        if(handler != null) {
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = ZFileOtherUtil.getMultimediaInfo(filePath, fileType instanceof  VideoType);
            handler.sendMessage(obtain);
        }
    }

    private void initView() {
        zfileDialogInfoFileName = (TextView) view.findViewById(R.id.zfile_dialog_info_fileName);
        zfileDialogInfoFileType = (TextView) view.findViewById(R.id.zfile_dialog_info_fileType);
        zfileDialogInfoFileSize = (TextView) view.findViewById(R.id.zfile_dialog_info_fileSize);
        zfileDialogInfoFileDate = (TextView) view.findViewById(R.id.zfile_dialog_info_fileDate);
        zfileDialogInfoFilePath = (TextView) view.findViewById(R.id.zfile_dialog_info_filePath);
        zfileDialogInfoMoreBox = (CheckBox) view.findViewById(R.id.zfile_dialog_info_moreBox);
        zfileDialogInfoMoreLayout = (LinearLayout) view.findViewById(R.id.zfile_dialog_info_moreLayout);
        zfileDialogInfoFileDurationLayout = (LinearLayout) view.findViewById(R.id.zfile_dialog_info_fileDurationLayout);
        zfileDialogInfoFileDuration = (TextView) view.findViewById(R.id.zfile_dialog_info_fileDuration);
        zfileDialogInfoFileFBLLayout = (LinearLayout) view.findViewById(R.id.zfile_dialog_info_fileFBLLayout);
        zfileDialogInfoFileFBL = (TextView) view.findViewById(R.id.zfile_dialog_info_fileFBL);
        zfileDialogInfoFileOther = (TextView) view.findViewById(R.id.zfile_dialog_info_fileOther);
        zfileDialogInfoDown = (Button) view.findViewById(R.id.zfile_dialog_info_down);
    }

    class InfoHandler extends Handler {

        private WeakReference<ZFileInfoDialog> week;

        public InfoHandler(ZFileInfoDialog dialog) {
            week = new WeakReference<ZFileInfoDialog>(dialog);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                ZFileInfoBean bean = (ZFileInfoBean) msg.obj;
                if(week.get() != null) {
                    if(fileType instanceof AudioType) {
                        zfileDialogInfoFileDuration.setText(bean.getDuration());
                    }else if(fileType instanceof VideoType) {
                        zfileDialogInfoFileDuration.setText(bean.getDuration());
                        zfileDialogInfoFileFBL.setText(String.format("%s * %s", bean.getWidth(), bean.getHeight()));
                    }
                }
            }
        }
    }
}
