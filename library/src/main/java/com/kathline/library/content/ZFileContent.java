package com.kathline.library.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kathline.library.R;
import com.kathline.library.common.ZFileManageDialog;
import com.kathline.library.common.ZFileManageHelp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ZFileContent {
    
    public static final String PNG = "png";
    
    public static final String JPG = "jpg";
    
    public static final String JPEG = "jpeg";
    
    public static final String GIF = "gif";
    
    public static final String MP3 = "mp3";
    
    public static final String AAC = "aac";
    
    public static final String WAV = "wav";
    
    public static final String MP4 = "mp4";
    
    public static final String _3GP = "3gp";
    
    public static final String TXT = "txt";
    
    public static final String XML = "xml";
    
    public static final String JSON = "json";
    
    public static final String DOC = "docx";
    
    public static final String XLS = "xlsx";
    
    public static final String PPT = "pptx";
    
    public static final String PDF = "pdf";
    
    public static final String ZIP = "zip";

    public static final String APK = "apk";
    /** 图片 */
    public static final int ZFILE_QW_PIC = 0;
    /** 媒体 */
    public static final int ZFILE_QW_MEDIA = 1;
    /** 文档 */
    public static final int ZFILE_QW_DOCUMENT = 2;
    /** 其他 */
    public static final int ZFILE_QW_OTHER = 3;

    /** 默认资源 */
    public static final int ZFILE_DEFAULT = -1;
    /** onActivityResult requestCode */
    public static final int ZFILE_REQUEST_CODE = 0x1000;
    /** onActivityResult resultCode */
    public static final int ZFILE_RESULT_CODE = 0x1001;

    /**
     * onActivityResult data key  --->>>
     * val list = data?.getParcelableArrayListExtra<[ZFileBean]>([ZFILE_SELECT_DATA_KEY])
     */
    public static final String ZFILE_SELECT_DATA_KEY = "ZFILE_SELECT_RESULT_DATA";
    public static final int COPY_TYPE = 8193;
    public static final int CUT_TYPE = 8194;
    public static final int DELTE_TYPE = 8195;
    public static final int ZIP_TYPE = 8196;
    public static final int FILE = 0;
    public static final int FOLDER = 1;
    
    public static final String QQ_PIC = "/storage/emulated/0/tencent/QQ_Images/";
    
    public static final String QQ_PIC_MOVIE = "/storage/emulated/0/Pictures/QQ/";
    
    public static final String QQ_DOWLOAD1 = "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/";
    
    public static final String QQ_DOWLOAD2 = "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQ_business/";
    
    public static final String WECHAT_FILE_PATH = "/storage/emulated/0/tencent/MicroMsg/";
    
    public static final String WECHAT_PHOTO_VIDEO = "WeiXin/";
    
    public static final String WECHAT_DOWLOAD = "Download/";
    
    public static final String LOG_TAG = "ZFileManager";
    
    public static final String ERROR_MSG = "fragmentOrActivity is not Activity or Fragment";
    
    public static final String QW_FILE_TYPE_KEY = "QW_fileType";
    
    public static final String FILE_START_PATH_KEY = "fileStartPath";

    public static ZFileManageHelp getZFileHelp() {
        return ZFileManageHelp.getInstance();
    }
    public static final ZFileConfiguration getZFileConfig() {
        return getZFileHelp().getConfiguration();
    }

    public static int getStatusBarHeight(Context context) {
        return getSystemHeight(context, "status_bar_height", "dimen");
    }

    public static int getSystemHeight(Context context, String name, String defType) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier(name, defType, "android"));
    }

    public static int[] getDisplay(Context context) {
        int[] size = new int[2];
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        size[0] = point.x;
        size[1] = point.y;
        return size;
    }

    public static void checkFragmentByTag(AppCompatActivity appCompatActivity, String tag) {
        Fragment fragmentByTag = appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if(fragmentByTag != null) {
            appCompatActivity.getSupportFragmentManager().beginTransaction().remove(fragmentByTag).commit();
        }
    }

    public static void jumpActivity(Activity activity, Class<?> clazz, ArrayMap<String, Object> map) {
        if(clazz != null) {
            Intent intent = new Intent(activity, clazz);
            if(map != null) {
                intent.putExtras(toBundle(map));
            }
            activity.startActivityForResult(intent, ZFILE_REQUEST_CODE);
        }
    }

    public static void jumpActivity(Fragment fragment, Class<?> clazz, ArrayMap<String, Object> map) {
        if(clazz != null) {
            Intent intent = new Intent(fragment.getContext(), clazz);
            if(map != null) {
                intent.putExtras(toBundle(map));
            }
            fragment.startActivityForResult(intent, ZFILE_REQUEST_CODE);
        }
    }

    public static Bundle toBundle(ArrayMap<String, Object> map) {
        Bundle bundle = new Bundle();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Integer) {
                bundle.putInt(key, (Integer) value);
            }else if(value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            }else if(value instanceof Float) {
                bundle.putFloat(key, (Float) value);
            }else if(value instanceof Long) {
                bundle.putLong(key, (Long) value);
            }else if(value instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) value);
            }else if(value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            }else if(value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            }
        }
        return bundle;
    }

    public static void setStatusBarTransparent(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setNeedWH(ZFileManageDialog dialog) {
        Context context = dialog.getContext();
        float width = getDisplay(context)[0] * 0.88F;
        dialog.getDialog().getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static final SwipeRefreshLayout property(SwipeRefreshLayout property, SwipeRefreshLayout.OnRefreshListener block, int color, boolean scale, int height) {
        int[] schemeColors = new int[1];
        Context context = property.getContext();
        int colorById = getColorById(context, color);
        property.setColorSchemeColors(schemeColors);
        if(scale) {
            property.setProgressViewEndTarget(scale, height);
        }
        property.setOnRefreshListener(block);
        return property;
    }

    public static SwipeRefreshLayout property(SwipeRefreshLayout property, SwipeRefreshLayout.OnRefreshListener block) {
        return property(property, block, R.color.zfile_base_color, false, 0);
    }

    public static final void toast(View toast, String msg) {
        toast(toast, msg, Toast.LENGTH_SHORT);
    }

    public static final void toast(View toast, String msg, int duration) {
        Context context = toast.getContext();
        toast(context, msg, duration);
    }

    public static final void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static final String[] getFilterArray(int getFilterArray) {
        String[] array;
        switch(getFilterArray) {
            case 0:
                array = new String[]{"png", "jpeg", "jpg", "gif"};
                break;
            case 1:
                array = new String[]{"mp4", "3gp"};
                break;
            case 2:
                array = new String[]{"txt", "json", "xml", "docx", "xlsx", "pptx", "pdf"};
                break;
            default:
                array = new String[]{""};
        }

        return array;
    }

    public static final int getColorById(Context context, int colorID) {
        return ContextCompat.getColor(context, colorID);
    }

    public static final String getStringById(Context context, int stringID) {
        return context.getResources().getString(stringID);
    }

    public static final String getFileType(File getFileType) {
        String var10000 = getFileType.getPath();
        return getFileType(var10000);
    }

    public static final String getFileType(String getFileType) {
        int preIndex = getFileType.lastIndexOf(".") + 1;
        int lastIndex = getFileType.length();
        String fileType = getFileType.substring(preIndex, lastIndex);
        return fileType;
    }

    public static boolean accept(String accept, String type) {
        return accept.endsWith(type.toLowerCase(Locale.CHINA)) || accept.endsWith(type.toUpperCase(Locale.CHINA));
    }

    public static String getFileName(String fileName) {
        return new File(fileName).getName();
    }

    public static File toFile(String filePath) {
        return new File(filePath);
    }

    public static ZFilePathBean toPathBean(ZFileBean bean) {
        ZFilePathBean zFilePathBean = new ZFilePathBean(bean.getFileName(), bean.getFilePath());
        return zFilePathBean;
    }

    public static ZFileQWBean toQWBean(ZFileBean bean, boolean isSelected) {
        return new ZFileQWBean(bean, isSelected);
    }

    public static ZFilePathBean toPathBean(File file) {
        ZFilePathBean zFilePathBean = new ZFilePathBean(file.getName(), file.getPath());
        return zFilePathBean;
    }

    public static ArrayList<ZFileBean> toFileList(ArrayMap<String, ZFileBean> map) {
        ArrayList<ZFileBean> list = new ArrayList<ZFileBean>();
        Set<Map.Entry<String, ZFileBean>> entries = map.entrySet();
        for (Map.Entry<String, ZFileBean> entry : entries) {
            list.add(entry.getValue());
        }
        return list;
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static String getSD_ROOT() {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getPath();
        return path;
    }

    public static final int getEmptyRes() {
        return getZFileConfig().getResources().getEmptyRes() == -1 ? R.drawable.ic_zfile_empty : getZFileConfig().getResources().getEmptyRes();
    }

    public static final int getFolderRes() {
        return getZFileConfig().getResources().getFolderRes() == -1 ? R.drawable.ic_zfile_folder : getZFileConfig().getResources().getFolderRes();
    }

    public static final int getLineColor() {
        return getZFileConfig().getResources().getLineColor() == -1 ? R.color.zfile_line_color : getZFileConfig().getResources().getLineColor();
    }

}
