package com.kathline.library.content;

public class ZFileQWBean {
    private ZFileBean zFileBean;
    private boolean isSelected;

    public ZFileQWBean(ZFileBean zFileBean, boolean isSelected) {
        this.zFileBean = zFileBean;
        this.isSelected = isSelected;
    }

    public ZFileBean getZFileBean() {
        return zFileBean;
    }

    public void setZFileBean(ZFileBean zFileBean) {
        this.zFileBean = zFileBean;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
