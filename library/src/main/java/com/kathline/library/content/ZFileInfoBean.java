package com.kathline.library.content;

public class ZFileInfoBean {
    private String duration;
    private String width;
    private String height;

    public ZFileInfoBean(String duration, String width, String height) {
        this.duration = duration;
        this.width = width;
        this.height = height;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ZFileInfoBean{" +
                "duration='" + duration + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}
