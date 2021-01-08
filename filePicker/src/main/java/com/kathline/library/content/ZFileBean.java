package com.kathline.library.content;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class ZFileBean implements Serializable, Parcelable {
    private String fileName;
    private boolean isFile;
    private String filePath;
    private String date;
    private String originalDate;
    private String size;
    private long originaSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getOriginaSize() {
        return originaSize;
    }

    public void setOriginaSize(long originaSize) {
        this.originaSize = originaSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeByte(this.isFile ? (byte) 1 : (byte) 0);
        dest.writeString(this.filePath);
        dest.writeString(this.date);
        dest.writeString(this.originalDate);
        dest.writeString(this.size);
        dest.writeLong(this.originaSize);
    }

    public ZFileBean() {
    }

    public ZFileBean(String fileName, boolean isFile, String filePath, String date, String originalDate, String size, long originaSize) {
        this.fileName = fileName;
        this.isFile = isFile;
        this.filePath = filePath;
        this.date = date;
        this.originalDate = originalDate;
        this.size = size;
        this.originaSize = originaSize;
    }

    protected ZFileBean(Parcel in) {
        this.fileName = in.readString();
        this.isFile = in.readByte() != 0;
        this.filePath = in.readString();
        this.date = in.readString();
        this.originalDate = in.readString();
        this.size = in.readString();
        this.originaSize = in.readLong();
    }

    public static final Creator<ZFileBean> CREATOR = new Creator<ZFileBean>() {
        @Override
        public ZFileBean createFromParcel(Parcel source) {
            return new ZFileBean(source);
        }

        @Override
        public ZFileBean[] newArray(int size) {
            return new ZFileBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZFileBean bean = (ZFileBean) o;
        return isFile == bean.isFile &&
                originaSize == bean.originaSize &&
                Objects.equals(fileName, bean.fileName) &&
                Objects.equals(filePath, bean.filePath) &&
                Objects.equals(date, bean.date) &&
                Objects.equals(originalDate, bean.originalDate) &&
                Objects.equals(size, bean.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, isFile, filePath, date, originalDate, size, originaSize);
    }
}
