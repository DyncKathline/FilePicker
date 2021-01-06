package com.kathline.library.content;

import android.os.Parcel;
import android.os.Parcelable;

import com.kathline.library.R;

import java.io.Serializable;

public class ZFileConfiguration implements Serializable {

    /**
     * QQ目录
     */
    public static final String QQ = "ZFILE_QQ_FILE_PATH";
    /**
     * 微信目录
     */
    public static final String WECHAT = "ZFILE_WECHAT_FILE_PATH";

    /**
     * 默认
     */
    public static final int BY_DEFAULT = 0x1000;
    /**
     * 根据名字
     */
    public static final int BY_NAME = 0x1001;
    /**
     * 根据最后修改时间
     */
    public static final int BY_DATE = 0x1003;
    /**
     * 根据大小
     */
    public static final int BY_SIZE = 0x1004;

    /**
     * 升序
     */
    public static final int ASC = 0x2001;
    /**
     * 降序
     */
    public static final int DESC = 0x2002;

    /**
     * 样式一
     */
    public static final int STYLE1 = 1;
    /**
     * 样式二
     */
    public static final int STYLE2 = 2;

    public static final String RENAME = "重命名";
    public static final String COPY = "复制";
    public static final String MOVE = "移动";
    public static final String DELETE = "删除";
    public static final String INFO = "查看详情";

    /**
     * 起始访问位置，空为SD卡根目录
     * 还可指定QQ或微信目录 see [QQ] [WECHAT]
     */
    String filePath = "";

    /**
     * 图片资源配置
     */
    ZFileResources resources = new ZFileResources();

    /**
     * 是否显示隐藏文件
     */
    boolean showHiddenFile = false;

    /**
     * 根据什么排序 see [BY_DEFAULT] [BY_NAME] [BY_DATE] [BY_SIZE]
     */
    int sortordBy = BY_DEFAULT;

    /**
     * 排序方式 see [ASC] [DESC]
     */
    int sortord = ASC;

    /**
     * 过滤规则，默认显示所有的文件类型
     * 如 arrayOf(PNG, JPG, JPEG, GIF) 只显示图片类型
     */
    String[] fileFilterArray = null;

    /**
     * 文件选取大小的限制，单位：M
     */
    int maxSize = 10;

    /**
     * 超过最大选择大小文字提醒
     */
    String maxSizeStr = "";

    /**
     * 最大选取数量
     */
    int maxLength = 9;

    /**
     * 超过最大选择数量文字提醒
     */
    String maxLengthStr = "";

    /**
     * 选中的样式 see [STYLE1] [STYLE2]
     */
    int boxStyle = STYLE2;

    /**
     * 是否需要长按事件
     */
    boolean needLongClick = true;

    /**
     * 默认只有文件才有长按事件
     * 长按暂不支持对于文件夹的操作，如有需要，请实现 ZFileOperateListener
     */
    boolean isOnlyFileHasLongClick = true;

    /**
     * 长按后需要显示的操作类型 see [RENAME] [COPY] [MOVE] [DELETE] [INFO]
     * 空默认为 arrayOf(RENAME, COPY, MOVE, DELETE, INFO)
     * 目前只可以是这几种类型，个数、顺序可以自定义，文字不支持自定义
     */
    String[] longClickOperateTitles = null;

    /**
     * 是否只需要显示文件夹
     */
    boolean isOnlyFolder = false;

    /**
     * 是否只需要显示文件
     */
    boolean isOnlyFile = false;

    /**
     * 打开文件需要 FileProvider 一般都是包名 + xxxFileProvider
     * 如果项目中已经存在或其他原因无法修改，请自己实现 ZFileOpenListener
     */
    String authority = "com.zp.zfile_manager.ZFileManagerProvider";

    /**
     * 是否显示日志
     */
    boolean showLog = true;

    public ZFileConfiguration() {
        this.filePath = "";
        this.resources = new ZFileConfiguration.ZFileResources();
        this.fileFilterArray = new String[0];
        this.maxSizeStr = "您只能选取小于" + this.maxSize + "M的文件";
        this.maxLength = 9;
        this.maxLengthStr = "您最多可以选取" + this.maxLength + "个文件";
        this.boxStyle = 1;
        this.needLongClick = true;
        this.isOnlyFileHasLongClick = true;
        this.longClickOperateTitles = new String[0];
        this.authority = "com.zp.zfile_manager.ZFileManagerProvider";
        this.showLog = true;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ZFileResources getResources() {
        return resources;
    }

    public void setResources(ZFileResources resources) {
        this.resources = resources;
    }

    public boolean isShowHiddenFile() {
        return showHiddenFile;
    }

    public void setShowHiddenFile(boolean showHiddenFile) {
        this.showHiddenFile = showHiddenFile;
    }

    public int getSortordBy() {
        return sortordBy;
    }

    public void setSortordBy(int sortordBy) {
        this.sortordBy = sortordBy;
    }

    public int getSortord() {
        return sortord;
    }

    public void setSortord(int sortord) {
        this.sortord = sortord;
    }

    public String[] getFileFilterArray() {
        return fileFilterArray;
    }

    public void setFileFilterArray(String[] fileFilterArray) {
        this.fileFilterArray = fileFilterArray;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getMaxSizeStr() {
        return maxSizeStr;
    }

    public void setMaxSizeStr(String maxSizeStr) {
        this.maxSizeStr = maxSizeStr;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getMaxLengthStr() {
        return maxLengthStr;
    }

    public void setMaxLengthStr(String maxLengthStr) {
        this.maxLengthStr = maxLengthStr;
    }

    public int getBoxStyle() {
        return boxStyle;
    }

    public void setBoxStyle(int boxStyle) {
        this.boxStyle = boxStyle;
    }

    public boolean isNeedLongClick() {
        return needLongClick;
    }

    public void setNeedLongClick(boolean needLongClick) {
        this.needLongClick = needLongClick;
    }

    public boolean isOnlyFileHasLongClick() {
        return isOnlyFileHasLongClick;
    }

    public void setOnlyFileHasLongClick(boolean onlyFileHasLongClick) {
        isOnlyFileHasLongClick = onlyFileHasLongClick;
    }

    public String[] getLongClickOperateTitles() {
        return longClickOperateTitles;
    }

    public void setLongClickOperateTitles(String[] longClickOperateTitles) {
        this.longClickOperateTitles = longClickOperateTitles;
    }

    public boolean isOnlyFolder() {
        return isOnlyFolder;
    }

    public void setOnlyFolder(boolean onlyFolder) {
        isOnlyFolder = onlyFolder;
    }

    public boolean isOnlyFile() {
        return isOnlyFile;
    }

    public void setOnlyFile(boolean onlyFile) {
        isOnlyFile = onlyFile;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public static final class Build {
        private String filePath = "";
        private ZFileConfiguration.ZFileResources resources;
        private boolean showHiddenFile;
        private int sortordBy = 4096;
        private int sortord = 8193;
        private String[] fileFilterArray;
        private int maxSize = 10;
        private String maxSizeStr;
        private int maxLength;
        private String maxLengthStr;
        private int boxStyle;
        private boolean needLongClick;
        private boolean isOnlyFileHasLongClick;
        private String[] longClickOperateTitles;
        private boolean isOnlyFolder;
        private boolean isOnlyFile;
        private String authority;
        private boolean showLog;

        public final ZFileConfiguration.Build filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public final ZFileConfiguration.Build resources(ZFileConfiguration.ZFileResources resources) {
            this.resources = resources;
            return this;
        }

        public final ZFileConfiguration.Build showHiddenFile(boolean showHiddenFile) {
            this.showHiddenFile = showHiddenFile;
            return this;
        }

        public final ZFileConfiguration.Build sortordBy(int sortordBy) {
            this.sortordBy = sortordBy;
            return this;
        }

        public final ZFileConfiguration.Build sortord(int sortord) {
            this.sortord = sortord;
            return this;
        }

        public final ZFileConfiguration.Build fileFilterArray(String[] fileFilterArray) {
            this.fileFilterArray = fileFilterArray;
            return this;
        }

        public final ZFileConfiguration.Build maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public final ZFileConfiguration.Build maxSizeStr(String maxSizeStr) {
            this.maxSizeStr = maxSizeStr;
            return this;
        }

        public final ZFileConfiguration.Build maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public final ZFileConfiguration.Build maxLengthStr(String maxLengthStr) {
            this.maxLengthStr = maxLengthStr;
            return this;
        }

        public final ZFileConfiguration.Build boxStyle(int boxStyle) {
            this.boxStyle = boxStyle;
            return this;
        }

        public final ZFileConfiguration.Build needLongClick(boolean needLongClick) {
            this.needLongClick = needLongClick;
            return this;
        }

        public final ZFileConfiguration.Build isOnlyFileHasLongClick(boolean isOnlyFileHasLongClick) {
            this.isOnlyFileHasLongClick = isOnlyFileHasLongClick;
            return this;
        }

        public final ZFileConfiguration.Build longClickOperateTitles(String[] longClickOperateTitles) {
            this.longClickOperateTitles = longClickOperateTitles;
            return this;
        }

        public final ZFileConfiguration.Build isOnlyFolder(boolean isOnlyFolder) {
            this.isOnlyFolder = isOnlyFolder;
            return this;
        }

        public final ZFileConfiguration.Build isOnlyFile(boolean isOnlyFile) {
            this.isOnlyFile = isOnlyFile;
            return this;
        }

        public final ZFileConfiguration.Build authority(String authority) {
            this.authority = authority;
            return this;
        }

        public final ZFileConfiguration.Build showLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public final ZFileConfiguration build() {
            ZFileConfiguration var1 = new ZFileConfiguration();
            var1.setFilePath(this.filePath);
            var1.setResources(this.resources);
            var1.setShowHiddenFile(this.showHiddenFile);
            var1.setSortordBy(this.sortordBy);
            var1.setSortord(this.sortord);
            var1.setFileFilterArray(this.fileFilterArray);
            var1.setMaxSize(this.maxSize);
            var1.setMaxSizeStr(this.maxSizeStr);
            var1.setMaxLength(this.maxLength);
            var1.setMaxLengthStr(this.maxLengthStr);
            var1.setBoxStyle(this.boxStyle);
            var1.setNeedLongClick(this.needLongClick);
            var1.setOnlyFileHasLongClick(this.isOnlyFileHasLongClick);
            var1.setLongClickOperateTitles(this.longClickOperateTitles);
            var1.setOnlyFolder(this.isOnlyFolder);
            var1.setOnlyFile(this.isOnlyFile);
            var1.setAuthority(this.authority);
            var1.setShowLog(this.showLog);
            return var1;
        }

        public Build() {
            this.filePath = "";
            this.resources = new ZFileConfiguration.ZFileResources();
            this.fileFilterArray = new String[0];
            this.maxSizeStr = "您只能选取小于" + this.maxSize + "M的文件";
            this.maxLength = 9;
            this.maxLengthStr = "您最多可以选取" + this.maxLength + "个文件";
            this.boxStyle = 1;
            this.needLongClick = true;
            this.isOnlyFileHasLongClick = true;
            this.longClickOperateTitles = new String[0];
            this.authority = "com.zp.zfile_manager.ZFileManagerProvider";
            this.showLog = true;
        }
    }

    public static class ZFileResources implements Serializable, Parcelable {
        private int audioRes = R.drawable.ic_zfile_audio;
        private int txtRes = R.drawable.ic_zfile_txt;
        private int pdfRes = R.drawable.ic_zfile_pdf;
        private int pptRes = R.drawable.ic_zfile_ppt;
        private int wordRes = R.drawable.ic_zfile_word;
        private int excelRes = R.drawable.ic_zfile_excel;
        private int zipRes = R.drawable.ic_zfile_zip;
        private int otherRes = R.drawable.ic_zfile_other;
        private int emptyRes = R.drawable.ic_zfile_empty;
        private int folderRes = R.drawable.ic_zfile_folder;
        private int lineColor = R.color.zfile_line_color;

        public int getAudioRes() {
            return audioRes;
        }

        public void setAudioRes(int audioRes) {
            this.audioRes = audioRes;
        }

        public int getTxtRes() {
            return txtRes;
        }

        public void setTxtRes(int txtRes) {
            this.txtRes = txtRes;
        }

        public int getPdfRes() {
            return pdfRes;
        }

        public void setPdfRes(int pdfRes) {
            this.pdfRes = pdfRes;
        }

        public int getPptRes() {
            return pptRes;
        }

        public void setPptRes(int pptRes) {
            this.pptRes = pptRes;
        }

        public int getWordRes() {
            return wordRes;
        }

        public void setWordRes(int wordRes) {
            this.wordRes = wordRes;
        }

        public int getExcelRes() {
            return excelRes;
        }

        public void setExcelRes(int excelRes) {
            this.excelRes = excelRes;
        }

        public int getZipRes() {
            return zipRes;
        }

        public void setZipRes(int zipRes) {
            this.zipRes = zipRes;
        }

        public int getOtherRes() {
            return otherRes;
        }

        public void setOtherRes(int otherRes) {
            this.otherRes = otherRes;
        }

        public int getEmptyRes() {
            return emptyRes;
        }

        public void setEmptyRes(int emptyRes) {
            this.emptyRes = emptyRes;
        }

        public int getFolderRes() {
            return folderRes;
        }

        public void setFolderRes(int folderRes) {
            this.folderRes = folderRes;
        }

        public int getLineColor() {
            return lineColor;
        }

        public void setLineColor(int lineColor) {
            this.lineColor = lineColor;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.audioRes);
            dest.writeInt(this.txtRes);
            dest.writeInt(this.pdfRes);
            dest.writeInt(this.pptRes);
            dest.writeInt(this.wordRes);
            dest.writeInt(this.excelRes);
            dest.writeInt(this.zipRes);
            dest.writeInt(this.otherRes);
            dest.writeInt(this.emptyRes);
            dest.writeInt(this.folderRes);
            dest.writeInt(this.lineColor);
        }

        public ZFileResources() {
            audioRes = R.drawable.ic_zfile_audio;
            txtRes = R.drawable.ic_zfile_txt;
            pdfRes = R.drawable.ic_zfile_pdf;
            pptRes = R.drawable.ic_zfile_ppt;
            wordRes = R.drawable.ic_zfile_word;
            excelRes = R.drawable.ic_zfile_excel;
            zipRes = R.drawable.ic_zfile_zip;
            otherRes = R.drawable.ic_zfile_other;
            emptyRes = R.drawable.ic_zfile_empty;
            folderRes = R.drawable.ic_zfile_folder;
            lineColor = R.color.zfile_line_color;
        }

        protected ZFileResources(Parcel in) {
            this.audioRes = in.readInt();
            this.txtRes = in.readInt();
            this.pdfRes = in.readInt();
            this.pptRes = in.readInt();
            this.wordRes = in.readInt();
            this.excelRes = in.readInt();
            this.zipRes = in.readInt();
            this.otherRes = in.readInt();
            this.emptyRes = in.readInt();
            this.folderRes = in.readInt();
            this.lineColor = in.readInt();
        }

        public static final Creator<ZFileResources> CREATOR = new Creator<ZFileResources>() {
            @Override
            public ZFileResources createFromParcel(Parcel source) {
                return new ZFileResources(source);
            }

            @Override
            public ZFileResources[] newArray(int size) {
                return new ZFileResources[size];
            }
        };
    }
}
