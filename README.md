[![Travis](https://img.shields.io/badge/ZFile-1.2.1-yellowgreen)](https://github.com/zippo88888888/ZFileManager)
[![Travis](https://img.shields.io/badge/API-19%2B-green)](https://github.com/zippo88888888/ZFileManager)
[![Travis](https://img.shields.io/badge/Apache-2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# 特点

### 该项目起源[ZFileManager](https://github.com/zippo88888888/ZFileManager)，由于他使用的是kotlin，我翻译成Java，后续加上链式回调和兼容Android 11，在此感谢此作者的开源精神，同时我也把修改后的项目也进行了开源。

### 1. 默认支持 音频，视频，图片，txt，zip，word，excel，ppt，pdf，apk 10种文件
### 2. 支持音频、视频播放，图片查看，zip解压，文件重命名、复制、移动、删除、查看详情
### 3. 支持查看指定文件类型，支持文件类型拓展
### 4. 支持多选，数量、文件大小限制、实时排序、指定文件路径访问
### 5. 支持QQ、微信文件选择（支持自定义获取）
### 6. 兼容 Android 11(这里使用的是StorageAccessFramework，原因是因为使用原来的已经获取不到数据了)

### 部分截图
<div align="left">
<img src = "https://upload-images.jianshu.io/upload_images/2147749-7c5bd57ee9f88c56.gif" width=180 >
</div>

## 基本使用 （[Java使用](https://github.com/zippo88888888/ZFileManager/blob/master/app/src/main/java/com/kathline/demo/JavaSampleActivity.java)）

#### Step 0. 添加依赖
```groovy
implementation 'org.dync.kathline:filePicker:1.2.0'
```

#### 注意：兼容Android 11
useSAF默认判断大于Android 11会自动给使用StorageAccessFramework，为了兼容过滤条件新增了一个[MimeType](https://github.com/zippo88888888/ZFileManager/blob/master/app/src/main/java/com/kathline/library/content/MimeType.java)进行过滤SAF打开文件管理器
```java

boolean useSAF = true;
final ZFileConfiguration configuration = new ZFileConfiguration.Build()
                .resources(resources)
                .useSAF(useSAF)
                .fileFilterArray(useSAF ? new String[]{MimeType.TYPE_pdf} : new String[]{ZFileContent.PDF})
```

#### Step 1. 实现ZFileImageListener，并在调用前或Application中初始化

```java

public class MyFileImageListener extends ZFileListener.ZFileImageListener {

    /**
     * 图片类型加载
     */
    @Override
    public void loadImage(ImageView imageView, File file) {
        Glide.with(imageView.getContext())
                .load(file)
                .apply(new RequestOptions().placeholder(R.drawable.ic_zfile_other).error(R.drawable.ic_zfile_other))
            .into(imageView);
    }
}

// 在调用前或Application中初始化 
ZFileContent.getZFileHelp().init(new MyFileImageListener());
```

#### Step 2. 在Activity或Fragment中使用

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_defaultMangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 打开文件管理
            getZFileHelp().start(MainActivity.this, new ProxyListener() {
                            @Override
                            public void onResult(int requestCode, int resultCode, Intent data) {
                                List<ZFileBean> fileList = ZFileManageHelp.getInstance().getSelectData(getBaseContext(), requestCode, resultCode, data);
                                if (fileList == null || fileList.size() <= 0) {
                                    return;
                                }
                                StringBuilder sb = new StringBuilder();
                                for (ZFileBean bean : fileList) {
                                    sb.append(bean.toString()).append("\n\n");
                                }
                                resultTxt.setText(sb.toString());
                            }
                        });
            }
        });
    }

}
```

### 文件类型拓展

#### Step 1. 新建一个类：ZFileType，重写里面的openFile()、loadingFile()方法

```java

// 自定义的类型
public static final String APK = "apk";

/**
 * 自定义Apk文件类型
 */
public class ApkType extends ZFileType {

    /**
     * 打开文件
     * @param filePath  文件路径
     * @param view      当前视图
     */
    @Override
    public void openFile(String filePath, View view) {
        Toast.makeText(view.getContext(), "打开自定义拓展文件", Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载文件
     * @param filePath 文件路径
     * @param pic      文件展示的图片
     */
    @Override
    public void loadingFile(String filePath, ImageView pic) {
        // 获取PackageManagerAPK的信息
        try {
            PackageManager packageManager = pic.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager
                    .getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                pic.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));
            }
        } catch (Throwable ignore) {
            pic.setImageResource(R.drawable.ic_launcher);
        }
    }
}

```

#### Step 3. 在调用前或Application中配置

```java

ZFileContent.getZFileHelp().setFileTypeListener(new MyFileTypeListener());
```

### QQ或微信文件选择

> QQ、微信默认根据时间倒序排序，不显示隐藏文件，过滤规则默认，只显示文件，不支持长按操作
其他配置与文件管理保持一致！具体可查看[这里](https://github.com/DyncKathline/FilePicker/blob/master/app/src/main/java/com/kathline/demo/SuperActivity.java)

```java

    super_qqTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开QQ文件选择
                ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                zFileConfig.setBoxStyle(ZFileConfiguration.STYLE2);
                zFileConfig.setFilePath(ZFileConfiguration.QQ);
                ZFileContent.getZFileHelp().setConfiguration(zFileConfig).start(this);
        }
    });

    super_wechatTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // 打开微信文件选择
                ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                zFileConfig.setBoxStyle(ZFileConfiguration.STYLE2);
                zFileConfig.setFilePath(ZFileConfiguration.WECHAT);
                ZFileContent.getZFileHelp().setConfiguration(zFileConfig).start(this);
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ZFileBean> list = ZFileContent.getZFileHelp().getSelectData(getBaseContext(), requestCode, resultCode, data);
        StringBuilder sb = new StringBuilder();
        for (ZFileBean bean : list) {
            sb.append(bean).append("\n\n");
        }
        superResultTxt.setText(sb.toString());
    }

```

#### 自定义文件获取
```java

/**
 * 获取文件
 * 此方式，排序、是否显示隐藏文件、过滤规则等等操作都需要自己实现
 * 获取配置信息：ZFileManageHelp.getInstance().getConfiguration()
 */
public class MyFileLoadListener implements ZFileLoadListener {

    /**
     * 获取手机里的文件List
     * @param filePath String          指定的文件目录访问，空为SD卡根目录
     * @return List<ZFileBean>         list
     */
    @Override
    public List<ZFileBean> getFileList(Context context, String filePath) {
        return getFileList(context, filePath);
    }

    private List<ZFileBean> getFileList(Context context, String filePath) {
         return null;
    }
}

// 在调用前或Application中配置
ZFileContent.getZFileHelp().setFileLoadListener(new MyFileLoadListener());


```

#### 自定义打开默认支持的文件

```java

public class MyFileOpenListener extends ZFileOpenListener {

    @Override public void openAudio(String filePath, View view) {} // 音频
    @Override public void openImage(String filePath, View view) {} // 图片
    @Override public void openVideo(String filePath, View view) {} // 视频
    @Override public void openTXT(String filePath, View view) {} // 文本
    @Override public void openZIP(String filePath, View view) {} // zip压缩包
    @Override public void openDOC(String filePath, View view) {} // word
    @Override public void openXLS(String filePath, View view) {} // xls
    @Override public void openPPT(String filePath, View view) {} // ppt
    @Override public void openPDF(String filePath, View view) {} // pdf
    @Override public void openOther(String filePath, View view) {} // 其他文件
}

// 在调用前或Application中配置
ZFileContent.getZFileHelp().setFileOpenListener(new MyFileOpenListener());

```
#### 自定义文件操作

```java

public class MyFileOperateListener extends ZFileOperateListener {

    /**
     * 文件重命名
     * @param filePath String   文件路径
     * @param context Context   Context
     * @param block (boolean：成功或失败；String：新名字)
     */
    public void renameFile(final String filePath, final Context context,
                               final Function3 block) {
       // 先弹出重命名Dialog，再执行重命名方法  
    }

    /**
     * 复制文件 耗时操作，建议放在非UI线程中执行
     * @param sourceFile String     源文件地址
     * @param targetFile String     目标文件地址
     * @param context Context       Context
     * @param block                 文件操作成功或失败后的监听
     */
    public void copyFile(String sourceFile, String targetFile, Context context, Function2 block) {
       ZFileUtil.copyFile(sourceFile, targetFile, context, block);
    }

    /**
     * 移动文件 耗时操作，建议放在非UI线程中执行
     */
    public void moveFile(String sourceFile, String targetFile, Context context, Function2 block) {
    
    }

    /**
     * 删除文件 耗时操作，建议放在非UI线程中执行
     */
    public void deleteFile(final String filePath, final Context context, final Function2 block) {
         
    }

    /**
     * 解压文件 耗时操作，建议放在非UI线程中执行
     */
    public void zipFile(String sourceFile, String targetFile, Context context, Function2 block) {
    }

    /**
     * 文件详情
     */
    public void fileInfo(ZFileBean bean, Context context) {
        
    }
}

// 在调用前或Application中配置
ZFileContent.getZFileHelp().setFileOperateListener(new MyFileOperateListener());

```

##### 更多操作请查看demo， ^_^ 如果觉得可以 star 一下哦
