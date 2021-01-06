package com.kathline.library.common;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.type.OtherType;

public class ZFileTypeManage {
    
    private ZFileType fileType = (ZFileType)(new OtherType());

    public static final ZFileTypeManage getTypeManager() {
        return ZFileTypeManage.Builder.INSTANCE.getMANAGER();
    }

    
    public final ZFileType getFileType() {
        return this.fileType;
    }

    public final void setFileType( ZFileType var1) {
        this.fileType = var1;
    }

    public final void openFile( String filePath,  View view) {
        this.fileType = this.getFileType(filePath);
        this.fileType.openFile(filePath, view);
    }

    public final void loadingFile( String filePath,  ImageView pic) {
        this.fileType = this.getFileType(filePath);
        this.fileType.loadingFile(filePath, pic);
    }

    public final void infoFile(ZFileBean bean, Context context) {
        this.fileType = this.getFileType(bean.getFilePath());
        this.fileType.infoFile(bean, context);
    }

    
    public final ZFileType getFileType( String filePath) {
        return ZFileContent.getZFileHelp().getFileTypeListener().getFileType(filePath);
    }

    private static final class Builder {
        
        private static final ZFileTypeManage MANAGER;
        
        public static final ZFileTypeManage.Builder INSTANCE;

        
        public final ZFileTypeManage getMANAGER() {
            return MANAGER;
        }

        static {
            ZFileTypeManage.Builder var0 = new ZFileTypeManage.Builder();
            INSTANCE = var0;
            MANAGER = new ZFileTypeManage();
        }
    }

}
