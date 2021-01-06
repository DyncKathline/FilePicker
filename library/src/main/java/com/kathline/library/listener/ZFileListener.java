package com.kathline.library.listener;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kathline.library.Function1;
import com.kathline.library.Function2;
import com.kathline.library.Function3;
import com.kathline.library.R;
import com.kathline.library.common.ZFileCommonDialog;
import com.kathline.library.common.ZFileType;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.type.AudioType;
import com.kathline.library.type.ImageType;
import com.kathline.library.type.OtherType;
import com.kathline.library.type.PdfType;
import com.kathline.library.type.PptType;
import com.kathline.library.type.TxtType;
import com.kathline.library.type.VideoType;
import com.kathline.library.type.WordType;
import com.kathline.library.type.XlsType;
import com.kathline.library.type.ZipType;
import com.kathline.library.ui.ZFileListActivity;
import com.kathline.library.ui.ZFilePicActivity;
import com.kathline.library.ui.ZFileVideoPlayActivity;
import com.kathline.library.ui.dialog.ZFileAudioPlayDialog;
import com.kathline.library.ui.dialog.ZFileInfoDialog;
import com.kathline.library.ui.dialog.ZFileRenameDialog;
import com.kathline.library.ui.dialog.ZFileSelectFolderDialog;
import com.kathline.library.util.ZFileHelp;
import com.kathline.library.util.ZFileLog;
import com.kathline.library.util.ZFileOpenUtil;
import com.kathline.library.util.ZFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZFileListener {
    /**
     * 获取文件数据
     */
    public interface ZFileLoadListener {

        /**
         * 获取手机里的文件List
         *
         * @param filePath String           指定的文件目录访问，空为SD卡根目录
         * @return MutableList<ZFileBean>?  list
         */
        List<ZFileBean> getFileList(Context context, String filePath);
    }

    /**
     * 图片或视频 显示
     */
    public abstract static class ZFileImageListener {

        /**
         * 图片类型加载
         */
        public abstract void loadImage(ImageView imageView, File file);

        /**
         * 视频类型加载
         */
        public void loadVideo(ImageView imageView, File file) {
            loadImage(imageView, file);
        }
    }

    /**
     * QQ 或 WeChat 获取
     */
    public abstract static class QWFileLoadListener {

        /**
         * 获取标题
         *
         * @return Array<String>
         */
        public String[] getTitles() {
            return null;
        }

        /**
         * 获取过滤规则
         *
         * @param fileType Int      文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
         */
        public abstract String[] getFilterArray(int fileType);

        /**
         * 获取 QQ 或 WeChat 文件路径
         *
         * @param qwType   String         QQ 或 WeChat  see [ZFileConfiguration.QQ] [ZFileConfiguration.WECHAT]
         * @param fileType Int          文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
         * @return MutableList<String>  文件路径集合（因为QQ或WeChat保存的文件可能存在多个路径）
         */
        public abstract List<String> getQWFilePathArray(String qwType, int fileType);

        /**
         * 获取数据
         *
         * @param fileType        Int                          文件类型 see [ZFILE_QW_PIC] [ZFILE_QW_MEDIA] [ZFILE_QW_DOCUMENT] [ZFILE_QW_OTHER]
         * @param qwFilePathArray MutableList<String>   QQ 或 WeChat 文件路径集合
         * @param filterArray     Array<String>             过滤规则
         */
        public abstract List<ZFileBean> getQWFileDatas(int fileType, List<String> qwFilePathArray, String[] filterArray);

    }

    /**
     * 文件类型
     */
    public static class ZFileTypeListener {

        public ZFileType getFileType(String filePath) {
            switch (ZFileHelp.getFileTypeBySuffix(filePath)) {
                case ZFileContent.PNG:
                case ZFileContent.JPG:
                case ZFileContent.JPEG:
                case ZFileContent.GIF:
                    return new ImageType();
                case ZFileContent.MP3:
                case ZFileContent.AAC:
                case ZFileContent.WAV:
                    return new AudioType();
                case ZFileContent.MP4:
                case ZFileContent._3GP:
                    return new VideoType();
                case ZFileContent.TXT:
                case ZFileContent.XML:
                case ZFileContent.JSON:
                    return new TxtType();
                case ZFileContent.ZIP:
                    return new ZipType();
                case ZFileContent.DOC:
                    return new WordType();
                case ZFileContent.XLS:
                    return new XlsType();
                case ZFileContent.PPT:
                    return new PptType();
                case ZFileContent.PDF:
                    return new PdfType();
                default:
                    return new OtherType();
            }
        }

    }

    /**
     * 打开文件
     */
    public static class ZFileOpenListener {

        /**
         * 打开音频
         */
        public void openAudio(String filePath, View view) {
            if (view.getContext() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                String tag = "ZFileAudioPlayDialog";
                ZFileContent.checkFragmentByTag((AppCompatActivity) view.getContext(), tag);
                ZFileAudioPlayDialog.newInstance(filePath).show(activity.getSupportFragmentManager(), tag);
            }
        }

        /**
         * 打开图片
         */
        public void openImage(String filePath, View view) {
            Intent intent = new Intent(view.getContext(), ZFilePicActivity.class);
            intent.putExtra("picFilePath", filePath);
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view,
                        ZFileContent.getStringById(view.getContext(), R.string.zfile_sharedElement_pic));
                view.getContext().startActivity(intent, activityOptions.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }

        /**
         * 打开视频
         */
        public void openVideo(String filePath, View view) {
            Intent intent = new Intent(view.getContext(), ZFileVideoPlayActivity.class);
            intent.putExtra("videoFilePath", filePath);
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view,
                        ZFileContent.getStringById(view.getContext(), R.string.zfile_sharedElement_video));
                view.getContext().startActivity(intent, activityOptions.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }

        /**
         * 打开Txt
         */
        public void openTXT(String filePath, View view) {
            ZFileOpenUtil.openTXT(filePath, view);
        }

        /**
         * 打开zip
         */
        public void openZIP(final String filePath, final View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("请选择");
            builder.setItems(new String[]{"打开", "解压"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        ZFileOpenUtil.openZIP(filePath, view);
                    } else {
                        zipSelect(filePath, view);
                    }
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        public void zipSelect(final String filePath, final View view) {
            if (view.getContext() instanceof ZFileListActivity) {
                final ZFileListActivity it = (ZFileListActivity) view.getContext();
                ZFileContent.checkFragmentByTag(it, "ZFileSelectFolderDialog");
                ZFileSelectFolderDialog dialog = ZFileSelectFolderDialog.newInstance("解压");
                dialog.setSelectFolderListener(new ZFileSelectFolderDialog.SelectFolderListener() {
                    @Override
                    public void invoke(String folder) {
                        ZFileContent.getZFileHelp().getFileOperateListener().zipFile(filePath, folder, view.getContext(), new Function2(){
                            @Override
                            public void invoke(boolean isSuccess) {
                                if (isSuccess) {
                                    ZFileLog.i("解压成功");
                                } else {
                                    ZFileLog.e("解压失败");
                                }
                                it.observer(isSuccess);
                            }
                        });

                    }
                });
                dialog.show(it.getSupportFragmentManager(), "ZFileSelectFolderDialog");
            }
        }

        /**
         * 打开word
         */
        public void openDOC(String filePath, View view) {
            ZFileOpenUtil.openDOC(filePath, view);
        }

        /**
         * 打开表格
         */
        public void openXLS(String filePath, View view) {
            ZFileOpenUtil.openXLS(filePath, view);
        }

        /**
         * 打开PPT
         */
        public void openPPT(String filePath, View view) {
            ZFileOpenUtil.openPPT(filePath, view);
        }

        /**
         * 打开PDF
         */
        public void openPDF(String filePath, View view) {
            ZFileOpenUtil.openPDF(filePath, view);
        }

        public void openOther(String filePath, View view) {
            ZFileLog.e(String.format("【%s】不支持预览该文件 ---> %s", ZFileContent.getFileType(filePath), filePath));
            ZFileContent.toast(view, "暂不支持预览该文件");
        }
    }

    /**
     * 文件操作
     */
    public static class ZFileOperateListener {

        /**
         * 文件重命名
         *
         * @param filePath String   文件路径
         * @param context  Context   Context
         * @param block    Function2<Boolean, String, Unit> Boolean：成功或失败；String：新名字
         */
        public void renameFile(final String filePath, final Context context,
                               final Function3 block) {
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                ZFileContent.checkFragmentByTag(activity, "ZFileRenameDialog");
                ZFileRenameDialog dialog = new ZFileRenameDialog();
                dialog.setReanameDownListener(new ZFileRenameDialog.ReanameDownListener() {
                    @Override
                    public void invoke(String name) {
                        ZFileUtil.renameFile(filePath, name, context, block);
                    }
                });
                dialog.show(activity.getSupportFragmentManager(), "ZFileRenameDialog");
            }
        }

        /**
         * 复制文件
         *
         * @param sourceFile String     源文件地址
         * @param targetFile String     目标文件地址
         * @param context    Context       Context
         */
        public void copyFile(String sourceFile, String targetFile, Context context, Function2 block) {
            ZFileUtil.copyFile(sourceFile, targetFile, context, block);
        }

        /**
         * 移动文件
         *
         * @param sourceFile String     源文件地址
         * @param targetFile String     目标文件地址
         * @param context    Context       Context
         */
        public void moveFile(String sourceFile, String targetFile, Context context, Function2 block) {
            ZFileUtil.cutFile(sourceFile, targetFile, context, block);
        }

        /**
         * 删除文件
         *
         * @param filePath String   源文件地址
         */
        public void deleteFile(final String filePath, final Context context, final Function2 block) {
            new ZFileCommonDialog(context).showDialog2(new Function1() {
                @Override
                public void invoke() {
                    ZFileUtil.deleteFile(filePath, context, block);
                }
            }, null, "您确定要删除吗？", "删除", "取消");
        }

        /**
         * 解压文件
         * 请注意，文件解压目前只支持压缩包里面只有一个文件的情况，多个暂不支持，如有需要，请自己实现
         *
         * @param sourceFile String     源文件地址
         * @param targetFile String     目标文件地址
         */
        public void zipFile(String sourceFile, String targetFile, Context context, Function2 block) {
            ZFileUtil.zipFile(sourceFile, targetFile, context, block);
        }

        /**
         * 文件详情
         */
        public void fileInfo(ZFileBean bean, Context context) {
            String tag = ZFileInfoDialog.class.getSimpleName();
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                ZFileContent.checkFragmentByTag(activity, tag);
                ZFileInfoDialog.newInstance(bean).show(activity.getSupportFragmentManager(), tag);
            }

        }
    }
}