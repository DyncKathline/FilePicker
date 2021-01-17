package com.kathline.library.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kathline.library.Function;
import com.kathline.library.Function2;
import com.kathline.library.Function3;
import com.kathline.library.R;
import com.kathline.library.common.ZFileActivity;
import com.kathline.library.common.ZFileAdapter;
import com.kathline.library.common.ZFileViewHolder;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.content.ZFilePathBean;
import com.kathline.library.ui.adapter.ZFileListAdapter;
import com.kathline.library.ui.dialog.ZFileSelectFolderDialog;
import com.kathline.library.ui.dialog.ZFileSortDialog;
import com.kathline.library.util.PermissionUtil;
import com.kathline.library.util.ZFileLog;
import com.kathline.library.util.ZFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZFileListActivity extends ZFileActivity {

    private boolean barShow = false;
    private ZFileAdapter<ZFilePathBean> filePathAdapter;
    private ZFileListAdapter fileListAdapter;

    private int index = 0;
    private String rootPath = "";// 根目录
    private String specifyPath = "";// 指定目录
    private String nowPath = "";// 当前目录
    private String[] titleArray;
    private ArrayList<String> backList;

    private int sortSelectId = R.id.zfile_sort_by_default; // 排序方式选中的ID
    private int sequenceSelectId = R.id.zfile_sequence_asc; // 顺序选中的ID


    private Toolbar zfileListToolBar;
    private RecyclerView zfileListPathRecyclerView;
    private SwipeRefreshLayout zfileListRefreshLayout;
    private RecyclerView zfileListListRecyclerView;
    private FrameLayout zfileListEmptyLayout;
    private ImageView zfileListEmptyPic;

    @Override
    public int getContentView() {
        return R.layout.activity_zfile_list;
    }

    @Override
    public void init(@Nullable Bundle var1) {
        initView();
        if (ZFileContent.getZFileConfig().getLongClickOperateTitles() != null) {
            titleArray = new String[]{
                    ZFileConfiguration.RENAME,
                    ZFileConfiguration.COPY,
                    ZFileConfiguration.MOVE,
                    ZFileConfiguration.DELETE,
                    ZFileConfiguration.INFO
            };
        } else {
            titleArray = ZFileContent.getZFileConfig().getLongClickOperateTitles();
        }

        backList = new ArrayList<>();
        setSortSelectId();
        specifyPath = getIntent().getStringExtra(ZFileContent.FILE_START_PATH_KEY);
        ZFileContent.getZFileConfig().setFilePath(specifyPath);
        rootPath = !TextUtils.isEmpty(specifyPath) ? specifyPath : "";
        backList.add(rootPath);
        nowPath = rootPath;
        zfileListToolBar.inflateMenu(R.menu.zfile_list_menu);
        zfileListToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                menuItemClick(item);
                return false;
            }
        });
        zfileListToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        zfileListEmptyPic.setImageResource(ZFileContent.getEmptyRes());
        setHiddenState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkHasPermission(); else initAll();
    }

    private void initView() {
        zfileListToolBar = (Toolbar) findViewById(R.id.zfile_list_toolBar);
        zfileListPathRecyclerView = (RecyclerView) findViewById(R.id.zfile_list_pathRecyclerView);
        zfileListRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.zfile_list_refreshLayout);
        zfileListListRecyclerView = (RecyclerView) findViewById(R.id.zfile_list_listRecyclerView);
        zfileListEmptyLayout = (FrameLayout) findViewById(R.id.zfile_list_emptyLayout);
        zfileListEmptyPic = (ImageView) findViewById(R.id.zfile_list_emptyPic);
    }

    private void initAll() {
        ZFileContent.property(zfileListRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(nowPath);
            }
        });
        initPathRecyclerView();
        initListRecyclerView();
    }

    /**
     * 返回当前的路径
     */
    private String getThisFilePath() {
        if (backList.size() == 0) {
            return null;
        } else {
            return backList.get(backList.size() - 1);
        }
    }

    private void setMenuState() {
        Menu menu = zfileListToolBar.getMenu();
        menu.findItem(R.id.menu_zfile_down).setVisible(barShow);
        menu.findItem(R.id.menu_zfile_px).setVisible(!barShow);
        menu.findItem(R.id.menu_zfile_show).setVisible(!barShow);
        menu.findItem(R.id.menu_zfile_hidden).setVisible(!barShow);
    }

    private boolean menuItemClick(MenuItem menu) {
        int itemId = menu.getItemId();
        if (itemId == R.id.menu_zfile_down) {
            ArrayList<ZFileBean> list = fileListAdapter.getSelectData();
            if (list == null || list.size() == 0) {
                zfileListToolBar.setTitle("文件管理");
                fileListAdapter.setManage(false);
                barShow = false;
                setMenuState();
            } else {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(ZFileContent.ZFILE_SELECT_DATA_KEY, list);
                setResult(ZFileContent.ZFILE_RESULT_CODE, intent);
                finish();
            }

            showSortDialog();
        } else if (itemId == R.id.menu_zfile_px) {
            showSortDialog();
        } else if (itemId == R.id.menu_zfile_show) {
            menu.setChecked(true);
            ZFileContent.getZFileConfig().setShowHiddenFile(true);
            getData(nowPath);
        } else if (itemId == R.id.menu_zfile_hidden) {
            menu.setChecked(true);
            ZFileContent.getZFileConfig().setShowHiddenFile(false);
            getData(nowPath);
        }
        return true;
    }

    private void initPathRecyclerView() {
        filePathAdapter = new ZFileAdapter<ZFilePathBean>(this, R.layout.item_zfile_path) {

            @Override
            protected void bindView(ZFileViewHolder holder, ZFilePathBean item, int position) {
                holder.setText(R.id.item_zfile_path_title, item.getFileName());
            }

            @Override
            public void addItem(int position, ZFilePathBean data) {
                boolean hasData = false;
                for (int i = 0; i < getDatas().size(); i++) {
                    ZFilePathBean bean = getDatas().get(i);
                    if (bean.getFilePath().equals(data.getFilePath())) {
                        hasData = true;
                        break;
                    }
                }
                if (!(hasData || data.getFilePath().equals(ZFileContent.getSD_ROOT()))) {
                    super.addItem(position, data);
                }
            }
        };
        zfileListPathRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        zfileListPathRecyclerView.setAdapter(filePathAdapter);
        getPathData();
    }

    private void getPathData() {
        String filePath = ZFileContent.getZFileConfig().getFilePath();
        ArrayList<ZFilePathBean> pathList = new ArrayList<ZFilePathBean>();
        if (TextUtils.isEmpty(filePath) || filePath.equals(ZFileContent.getSD_ROOT())) {
            pathList.add(new ZFilePathBean("根目录", "root"));
        } else {
            pathList.add(new ZFilePathBean(String.format("指定目录%s", ZFileContent.getFileName(filePath)), filePath));
        }
        filePathAdapter.addAll(pathList);
    }

    private void initListRecyclerView() {
        fileListAdapter = new ZFileListAdapter(this);
        fileListAdapter.setItemClickByAnim(new ZFileListAdapter.ItemClickByAnim() {
            @Override
            public void onClick(View view, int position, ZFileBean bean) {
                if (bean.isFile()) {
                    /*if (fileListAdapter.isManage) {
                        fileListAdapter.boxLayoutClick(position, item)
                    } else {
                        ZFileUtil.openFile(item.filePath, v)
                    }*/
                    ZFileUtil.openFile(bean.getFilePath(), view);
                } else {
                    ZFileLog.i(String.format("进入 %s", bean.getFilePath()));
                    backList.add(bean.getFilePath());
                    filePathAdapter.addItem(filePathAdapter.getItemCount(), ZFileContent.toPathBean(bean));
//                    zfileListPathRecyclerView.scrollToPosition(filePathAdapter.getItemCount() - 1);
                    getData(bean.getFilePath());
                    nowPath = bean.getFilePath();
                }
            }
        });
        fileListAdapter.setOnLongClickListener(new ZFileAdapter.OnLongClickListener<ZFileBean>() {
            @Override
            public boolean onLongClick(View view, int position, ZFileBean data) {
                if (fileListAdapter.isManage()) {
                    return false;
                } else {
                    if (ZFileContent.getZFileConfig().isNeedLongClick()) {
                        if (ZFileContent.getZFileConfig().isOnlyFileHasLongClick()) {
                            if (data.isFile()) {
                                return showSelectDialog(position, data);
                            }
                            else {
                                return false;
                            }
                        } else {
                            return showSelectDialog(position, data);
                        }
                    } else {
                        return false;
                    }
                }
            }
        });
        fileListAdapter.setChangeListener(new ZFileListAdapter.ChangeListener() {
            @Override
            public void change(boolean isManage, int size) {
                if (isManage) {
                    if (barShow) {
                        zfileListToolBar.setTitle(String.format("已选中%d个文件", size));
                    } else {
                        barShow = true;
                        zfileListToolBar.setTitle("已选中0个文件");
                        setMenuState();
                    }
                }
            }
        });
        zfileListListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        zfileListListRecyclerView.setAdapter(fileListAdapter);
        getData(ZFileContent.getZFileConfig().getFilePath());
        index ++;
    }

    private void getData(String filePath) {
        zfileListRefreshLayout.setRefreshing(true);
        String key;
        if (TextUtils.isEmpty(filePath)) {
            key = ZFileContent.getSD_ROOT();
        } else {
            key = filePath;
        }
        ZFileContent.getZFileConfig().setFilePath(filePath);
        if (index != 0) {
            filePathAdapter.addItem(filePathAdapter.getItemCount(), ZFileContent.toPathBean(new File(key)));
            zfileListPathRecyclerView.scrollToPosition(filePathAdapter.getItemCount() - 1);
        }
        ZFileUtil.getList(context, new Function() {
            @Override
            public void invoke(List<ZFileBean> list) {
                if (list == null || list.size() == 0) {
                    fileListAdapter.clear();
                    zfileListEmptyLayout.setVisibility(View.VISIBLE);
                } else {
                    fileListAdapter.setDatas(list);
                    zfileListListRecyclerView.scrollToPosition(0);
                    zfileListEmptyLayout.setVisibility(View.GONE);
                }
                zfileListRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean showSelectDialog(final int index, final ZFileBean item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择");
        builder.setItems(titleArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jumpByWhich(item, which, index);
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
        return true;
    }

    private final String TAG = ZFileSelectFolderDialog.class.getSimpleName();

    private void jumpByWhich(final ZFileBean item, final int which, final int index) {
        switch (titleArray[which]) {
            case ZFileConfiguration.RENAME: {
                ZFileContent.getZFileHelp().getFileOperateListener().renameFile(item.getFilePath(), this, new Function3() {
                    @Override
                    public void invoke(boolean isSuccess, String name) {
                        if (isSuccess) {
                            File oldFile = new File(item.getFilePath());
                            String oldFileType = ZFileContent.getFileType(oldFile);
                            String oldPath = oldFile.getPath().substring(0, oldFile.getPath().lastIndexOf("/") + 1);
                            String newFilePath = oldPath + name + "." + oldFileType;
                            ZFileBean zFileBean = fileListAdapter.getItem(index);
                            zFileBean.setFilePath(newFilePath);
                            zFileBean.setFileName(name + "." + oldFileType);
                            fileListAdapter.notifyItemChanged(index);
                        }
                    }
                });
            }
            break;
            case ZFileConfiguration.COPY:
            case ZFileConfiguration.MOVE: {
                ZFileContent.checkFragmentByTag(activity, TAG);
                ZFileSelectFolderDialog dialog = ZFileSelectFolderDialog.newInstance(titleArray[which]);
                dialog.setSelectFolderListener(new ZFileSelectFolderDialog.SelectFolderListener() {
                    @Override
                    public void invoke(String folder) {
                        doSth(item, folder, titleArray[which], index);
                    }
                });
                dialog.show(getSupportFragmentManager(), TAG);
            }
            break;
            case ZFileConfiguration.DELETE:
                ZFileContent.getZFileHelp().getFileOperateListener().deleteFile(
                        item.getFilePath(),
                        this,
                        new Function2() {
                            @Override
                            public void invoke(boolean isSuccess) {
                                if (isSuccess) {
                                    if (fileListAdapter != null) {
                                        fileListAdapter.remove(index);
                                    }
                                    ZFileLog.i("文件删除成功");
                                } else {
                                    ZFileLog.i("文件删除失败");
                                }
                            }
                        }
                );
                break;
            case ZFileConfiguration.INFO:
                ZFileUtil.infoFile(item, this);
                break;
            default:
                throw new IllegalArgumentException("ZFileConfiguration longClickOperateTitles ERROR");
        }
    }

    private void doSth(ZFileBean item, String targetPath, String type, final int position) {
        if (type.equals(ZFileConfiguration.COPY)) { // 复制文件
            ZFileContent.getZFileHelp().getFileOperateListener().copyFile(item.getFilePath(), targetPath, this, new Function2() {
                @Override
                public void invoke(boolean isSuccess) {
                    if (isSuccess) {
                        ZFileLog.i("文件复制成功");
                    } else {
                        ZFileLog.e("文件复制失败");
                    }
                }
            });
        } else { // 移动文件
            ZFileContent.getZFileHelp().getFileOperateListener().moveFile(item.getFilePath(), targetPath, this, new Function2() {
                @Override
                public void invoke(boolean isSuccess) {
                    if (isSuccess) {
                        if (fileListAdapter != null) {
                            fileListAdapter.remove(position);
                        }
                        ZFileLog.i("文件移动成功");
                    } else {
                        ZFileLog.e("文件移动失败");
                    }
                }
            });
        }
    }

    private void showSortDialog() {
        String tag = ZFileSortDialog.class.getSimpleName();
        ZFileContent.checkFragmentByTag(activity, tag);
        ;
        ZFileSortDialog dialog = ZFileSortDialog.newInstance(sortSelectId, sequenceSelectId);
        dialog.show(getSupportFragmentManager(), tag);
        dialog.setCheckedChangedListener(new ZFileSortDialog.CheckedChangedListener() {
            @Override
            public void onCheckedChange(int sortId, int sequenceId) {
                sortSelectId = sortId;
                sequenceSelectId = sequenceId;
                int sortordByWhat;
                if (sortId == R.id.zfile_sort_by_default) {
                    sortordByWhat = ZFileConfiguration.BY_DEFAULT;
                } else if (sortId == R.id.zfile_sort_by_name) {
                    sortordByWhat = ZFileConfiguration.BY_NAME;
                } else if (sortId == R.id.zfile_sort_by_date) {
                    sortordByWhat = ZFileConfiguration.BY_DATE;
                } else if (sortId == R.id.zfile_sort_by_size) {
                    sortordByWhat = ZFileConfiguration.BY_SIZE;
                } else {
                    sortordByWhat = ZFileConfiguration.BY_DEFAULT;
                }
                int sortord;
                if (sequenceId == R.id.zfile_sequence_asc) {
                    sortord = ZFileConfiguration.ASC;
                } else if (sequenceId == R.id.zfile_sequence_desc) {
                    sortord = ZFileConfiguration.DESC;
                } else {
                    sortord = ZFileConfiguration.ASC;
                }
                ZFileConfiguration zFileConfig = ZFileContent.getZFileConfig();
                zFileConfig.setSortordBy(sortordByWhat);
                zFileConfig.setSortord(sortord);
                getData(nowPath);
            }
        });
    }

    @Override
    public void onBackPressed() {
        String path = getThisFilePath();
        if (TextUtils.isEmpty(path) || (path != null && path.equals(rootPath))) { // 根目录
            if (barShow) {  // 存在编辑状态
                zfileListToolBar.setTitle("文件管理");
                fileListAdapter.setManage(false);
                barShow = false;
                setMenuState();
            } else {
                super.onBackPressed();
            }
        } else { // 返回上一级
            // 先清除当前一级的数据
            backList.remove(backList.size() - 1);
            String lastPath = getThisFilePath();
            getData(lastPath);
            nowPath = lastPath;
            filePathAdapter.remove(filePathAdapter.getItemCount() - 1);
//            zfileListPathRecyclerView.scrollToPosition(filePathAdapter.getItemCount() - 1);
        }
    }

    private void checkHasPermission() {
        PermissionUtil.getInstance().with(this)
                .requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
                    @Override
                    public void onGranted() {
                        initAll();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        ZFileContent.toast(getBaseContext(), "权限申请失败");
                        finish();
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {

                    }
                });
    }

    private void setSortSelectId() {
        switch (ZFileContent.getZFileConfig().getSortordBy()) {
            case ZFileConfiguration.BY_NAME:
                sortSelectId = R.id.zfile_sort_by_name;
                break;
            case ZFileConfiguration.BY_SIZE:
                sortSelectId = R.id.zfile_sort_by_size;
                break;
            case ZFileConfiguration.BY_DATE:
                sortSelectId = R.id.zfile_sort_by_date;
                break;
            default:
                sortSelectId = R.id.zfile_sort_by_default;
        }
        switch (ZFileContent.getZFileConfig().getSortord()) {
            case ZFileConfiguration.DESC:
                sequenceSelectId = R.id.zfile_sequence_desc;
                break;
            default:
                sequenceSelectId = R.id.zfile_sequence_asc;
        }
    }

    private void setHiddenState() {
        zfileListToolBar.post(new Runnable() {
            @Override
            public void run() {
                Menu menu = zfileListToolBar.getMenu();
                MenuItem showMenuItem = menu.findItem(R.id.menu_zfile_show);
                MenuItem hiddenMenuItem = menu.findItem(R.id.menu_zfile_hidden);
                if (ZFileContent.getZFileConfig().isShowHiddenFile()) {
                    showMenuItem.setChecked(true);
                } else {
                    hiddenMenuItem.setChecked(true);
                }
            }
        });
    }

    public void observer(boolean isSuccess) {
        if (isSuccess) {
            getData(nowPath);
        }
    }

    @Override
    protected void onDestroy() {
        ZFileUtil.resetAll();
        super.onDestroy();
        if(fileListAdapter != null) {
            fileListAdapter.reset();
        }
        backList.clear();
    }
}
