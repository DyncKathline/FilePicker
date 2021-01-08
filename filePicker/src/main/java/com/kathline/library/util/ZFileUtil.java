package com.kathline.library.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.kathline.library.Function;
import com.kathline.library.Function1;
import com.kathline.library.Function2;
import com.kathline.library.Function3;
import com.kathline.library.async.ZFileThread;
import com.kathline.library.common.ZFileTypeManage;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.listener.ZFileQWFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZFileUtil {

    /**
     * 获取文件
     */
    public static void getList(Context context, Function bolck) {
        new ZFileThread(context, bolck).start(ZFileContent.getZFileConfig().getFilePath());
    }

    /**
     * 打开文件
     */
    public static void openFile(String filePath, View view) {
        ZFileTypeManage.getTypeManager().openFile(filePath, view);
    }

    /**
     * 查看文件详情
     */
    public static void infoFile(ZFileBean bean, Context context) {
        ZFileTypeManage.getTypeManager().infoFile(bean, context);
    }

    /**
     * 重命名文件
     */
    public static void renameFile(final String filePath,
                                  final String newName,
                                  Context context,
                                  final Function3 block) {
        if(context instanceof Activity) {
            final Activity activity = (Activity) context;
            final ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("重命名中，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isSuccess = false;
                    try {
                        File oldFile = new File(filePath);
                        String oldFileType = ZFileContent.getFileType(oldFile);
                        String oldPath = oldFile.getPath().substring(0, oldFile.getPath().lastIndexOf("/") + 1);
                        File newFile = new File(oldPath+newName+"."+oldFileType);
                        isSuccess = oldFile.renameTo(newFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final boolean finalIsSuccess = isSuccess;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            final String msg = finalIsSuccess ? "重命名成功" : "重命名失败";
                            ZFileContent.toast(activity, msg);
                            block.invoke(finalIsSuccess, newName);
                        }
                    });
                }
            }).start();
        }

    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath, Context context, Function2 block) {
        ZFileLog.i(String.format("删除文件的目录：%s", filePath));
        callFileByType(filePath, "", context, ZFileContent.DELTE_TYPE, block);
    }

    /**
     * 复制文件
     */
    public static void copyFile(String filePath, String outPath, Context context, Function2 block) {
        ZFileLog.i(String.format("源文件目录：%s", filePath));
        ZFileLog.i(String.format("复制文件目录：%s", outPath));
        callFileByType(filePath, outPath, context, ZFileContent.COPY_TYPE, block);
    }

    /**
     * 剪切文件
     */
    public static void cutFile(String filePath, String outPath, Context context, Function2 block) {
        ZFileLog.i(String.format("源文件目录：%s", filePath));
        ZFileLog.i(String.format("移动目录：%s", outPath));
        callFileByType(filePath, outPath, context, ZFileContent.CUT_TYPE, block);
    }

    /**
     * 解压文件
     */
    public static void zipFile(String filePath, String outZipPath, Context context, Function2 block) {
        ZFileLog.i(String.format("源文件目录：%s", filePath));
        ZFileLog.i(String.format("解压目录：%s", outZipPath));
        callFileByType(filePath, outZipPath, context, ZFileContent.ZIP_TYPE, block);
    }

    private static void callFileByType(
            final String filePath,
            final String outPath,
            Context context,
            final int type,
            final Function2 block
    ) {
        String msg = "";
        switch (type) {
            case ZFileContent.COPY_TYPE:
                msg = ZFileConfiguration.COPY;
                break;
            case ZFileContent.CUT_TYPE:
                msg = ZFileConfiguration.MOVE;
                break;
            case ZFileContent.DELTE_TYPE:
                msg = ZFileConfiguration.DELETE;
                break;
            default:
                msg = "解压";
        }
        final Activity activity = (Activity) context;
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(String.format("%s中，请稍后...", msg));
        dialog.setCancelable(false);
        dialog.show();
        final String finalMsg = msg;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = false;
                switch (type) {
                    case ZFileContent.COPY_TYPE:
                        isSuccess = copyFile(filePath, outPath);
                    break;
                    case ZFileContent.CUT_TYPE:
                        isSuccess = cutFile(filePath, outPath);
                        break;
                    case ZFileContent.DELTE_TYPE:
                        isSuccess = new File(filePath).delete();
                        break;
                    default:
                        isSuccess = extractFile(filePath, outPath);
                }
                final boolean finalIsSuccess = isSuccess;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        String str = finalIsSuccess ? finalMsg + "成功" : finalMsg + "失败";
                        ZFileContent.toast(activity, str);
                        block.invoke(finalIsSuccess);
                    }
                });
            }
        }).start();
    }

    /**
     * 复制文件
     */
    private static boolean copyFile(String sourceFile, String targetFile) {
        boolean success = true;
        File oldFile = new File(sourceFile);
        File outFile = new File(targetFile + "/" + oldFile.getName());

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = (new FileInputStream(oldFile)).getChannel();
            outputChannel = (new FileOutputStream(outFile)).getChannel();
            if (outputChannel != null) {
                outputChannel.transferFrom((ReadableByteChannel)inputChannel, 0L, inputChannel != null ? inputChannel.size() : 0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return success;
        }
    }

    /**
     * 剪切文件
     */
    private static boolean cutFile(String sourceFile, String targetFile) {
        boolean copySuccess = copyFile(sourceFile, targetFile);
        boolean delSuccess = false;
        try {
            delSuccess = new File(sourceFile).delete();
        } catch (Exception e) {
            e.printStackTrace();
            delSuccess = false;
        } finally {
            return copySuccess && delSuccess;
        }
    }

    /**
     * 解压文件
     */
    private static boolean extractFile(String zipFileName, String outPutDir) {
        boolean flag = true;
        try {
            ZipUtils.unZipFolder(zipFileName, outPutDir);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 获取文件大小
     */
    public static String getFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        Double byte_size = Double.valueOf(df.format(fileS));
        if (byte_size < 1024) {
            return byte_size + " B";
        }
        Double kb_size = Double.valueOf(df.format(fileS / 1024d));
        if (kb_size < 1024) {
            return kb_size + " KB";
        }
        Double mb_size = Double.valueOf(df.format(fileS / 1048576d));
        if (mb_size < 1024) {
            return mb_size + " MB";
        }
        Double gb_size = Double.valueOf(df.format(fileS / 1073741824d));
        if (gb_size < 1024) {
            return gb_size + " GB";
        }
        return ">1TB";
    }

    /**
     * 获取QQ微信文件
     * @param type              文件类型
     * @param filePathArray     路径
     * @param filterArray       过滤规则
     */
    public static List<ZFileBean> getQWFileData(int type, List<String> filePathArray, String[] filterArray) {
        ArrayList<ZFileBean> list = new ArrayList<ZFileBean>();
        File[] listFiles = null;
        if (filePathArray.size() <= 1) {
            File file = new File(filePathArray.get(0));
            if (file.exists()) {
                listFiles = file.listFiles(new ZFileQWFilter(filterArray, type == ZFileContent.ZFILE_QW_OTHER));
            }
        } else {
            File file1 = new File(filePathArray.get(0));
            File[] list1 = null;
            if (file1.exists()) {
                list1 = file1.listFiles(new ZFileQWFilter(filterArray, type == ZFileContent.ZFILE_QW_OTHER));
            } /*else {
                ZFileLog.e("路径 ${filePathArray[0]} 不存在")
            }*/
            File[] list2 = null;
            File file2 = new File(filePathArray.get(1));
            if (file2.exists()) {
                list2 = file2.listFiles(new ZFileQWFilter(filterArray, type == ZFileContent.ZFILE_QW_OTHER));
            } /*else {
                ZFileLog.e("路径 ${filePathArray[1]} 不存在")
            }*/
            if (list1 != null && list2 != null) {
                listFiles = ZFileContent.concatAll(list1, list2);
            } else {
                if (list1 != null) {
                    listFiles = list1;
                }
                if (list2 != null) {
                    listFiles = list2;
                }
            }
        }

        for (File it: listFiles) {
            if (!it.isHidden()) {
                ZFileBean bean = new ZFileBean(
                        it.getName(),
                        it.isFile(),
                        it.getPath(),
                        ZFileOtherUtil.getFormatFileDate(it.lastModified()),
                        it.lastModified() + "",
                        getFileSize(it.length()),
                        it.length()
                );
                list.add(bean);
            }
        }
        Collections.sort(list, new Comparator<ZFileBean>() {
            @Override
            public int compare(ZFileBean o1, ZFileBean o2) {
                return (int) (o1.getOriginaSize() - o2.getOriginaSize());
            }
        });
        return list;
    }

    public static void resetAll() {
        ZFileConfiguration fileConfig = ZFileContent.getZFileConfig();
        fileConfig.setFilePath("");
        /*fileConfig.setResources(new ZFileConfiguration.ZFileResources());
        fileConfig.setShowHiddenFile(false);
        fileConfig.setSortordBy(ZFileConfiguration.BY_DEFAULT);
        fileConfig.setSortord(ZFileConfiguration.ASC);
        fileConfig.setFileFilterArray(null);
        fileConfig.setMaxSize(10);
        fileConfig.setMaxSizeStr(String.format("您只能选取小于%dM的文件", fileConfig.getMaxSize()));
        fileConfig.setMaxLength(9);
        fileConfig.setMaxLengthStr(String.format("您最多可以选取%d个文件", fileConfig.getMaxLength()));
        fileConfig.setBoxStyle(ZFileConfiguration.STYLE2);
        fileConfig.setNeedLongClick(true);
        fileConfig.setOnlyFileHasLongClick(true);
        fileConfig.setLongClickOperateTitles(null);
        fileConfig.setOnlyFolder(false);
        fileConfig.setOnlyFile(false);*/
    }


}