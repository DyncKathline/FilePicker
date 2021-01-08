package com.kathline.library.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kathline.library.R;
import com.kathline.library.common.ZFileActivity;
import com.kathline.library.content.ZFileBean;
import com.kathline.library.content.ZFileConfiguration;
import com.kathline.library.content.ZFileContent;
import com.kathline.library.content.ZFileQWBean;
import com.kathline.library.listener.ZFileListener;
import com.kathline.library.util.PermissionUtil;
import com.kathline.library.util.ZFileUtil;

import java.util.ArrayList;
import java.util.List;

public class ZFileQWActivity extends ZFileActivity implements ViewPager.OnPageChangeListener {
    private Toolbar zfileQwToolBar;
    private TabLayout zfileQwTabLayout;
    private ViewPager zfileQwViewPager;

    private ZFileQWAdapter vpAdapter;
    private boolean isManage;
    private ArrayMap<String, ZFileBean> selectArray = new ArrayMap<>();

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_zfile_qw;
    }

    @Override
    public void init(@Nullable Bundle var1) {
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkHasPermission();
        } else {
            initAll();
        }
    }

    private void initView() {
        zfileQwToolBar = (Toolbar) findViewById(R.id.zfile_qw_toolBar);
        zfileQwTabLayout = (TabLayout) findViewById(R.id.zfile_qw_tabLayout);
        zfileQwViewPager = (ViewPager) findViewById(R.id.zfile_qw_viewPager);
    }

    private void initAll() {
        String type = ZFileContent.getZFileConfig().getFilePath();
        zfileQwToolBar.setTitle(type == ZFileConfiguration.QQ ? "QQ文件" : "微信文件");
        zfileQwToolBar.inflateMenu(R.menu.zfile_qw_menu);
        zfileQwToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                menuItemClick(item);
                return false;
            }
        });
        zfileQwToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        zfileQwViewPager.addOnPageChangeListener(this);
        zfileQwTabLayout.setupWithViewPager(zfileQwViewPager);
        vpAdapter = new ZFileQWAdapter(type, isManage, this, getSupportFragmentManager());
        zfileQwViewPager.setAdapter(vpAdapter);
    }

    public void observer(ZFileQWBean bean) {
        ZFileBean item = bean.getZFileBean();
        if (bean.isSelected()) {
            int size = selectArray.size();
            if (size >= ZFileContent.getZFileConfig().getMaxLength()) {
                ZFileContent.toast(this, ZFileContent.getZFileConfig().getMaxLengthStr());
                getVPFragment(zfileQwViewPager.getCurrentItem()).removeLastSelectData(bean.getZFileBean());
            } else {
                selectArray.put(item.getFilePath(), item);
            }
        } else {
            selectArray.remove(item.getFilePath());
        }
        zfileQwToolBar.setTitle(String.format("已选中%d个文件", selectArray.size()));
        isManage = true;
        getMenu().setVisible(true);
    }

    private MenuItem getMenu() {
        return zfileQwToolBar.getMenu().findItem(R.id.menu_zfile_qw_down);
    }

    private boolean menuItemClick(MenuItem menu) {
        if (menu.getItemId() == R.id.menu_zfile_qw_down) {
            if (selectArray == null || selectArray.isEmpty()) {
                for (int i = 0; i < vpAdapter.list.size(); i++) {
                    getVPFragment(i).resetAll();
                }
                isManage = false;
                getMenu().setVisible(false);
                zfileQwToolBar.setTitle(ZFileContent.getZFileConfig().getFilePath().equals(ZFileConfiguration.QQ) ? "QQ文件" : "微信文件");
            } else {
                Intent data = new Intent();
                data.putParcelableArrayListExtra(ZFileContent.ZFILE_SELECT_DATA_KEY, ZFileContent.toFileList(selectArray));
                setResult(ZFileContent.ZFILE_RESULT_CODE, data);
                finish();
            }
        }
        return true;
    }

    private ZFileQWFragment getVPFragment(int currentItem) {
        long fragmentId = vpAdapter.getItemId(currentItem);
        String tag = "android:switcher:" + zfileQwViewPager.getId() + ":" + fragmentId;
        return (ZFileQWFragment) getSupportFragmentManager().findFragmentByTag(tag);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isManage = false;
        selectArray.clear();
        ZFileUtil.resetAll();
    }

    static class ZFileQWAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> list;
        String[] titles;

        public ZFileQWAdapter(String type, boolean isManger, Context context, FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            titles = new String[]{ZFileContent.getStringById(context, R.string.zfile_pic),
                    ZFileContent.getStringById(context, R.string.zfile_video),
                    ZFileContent.getStringById(context, R.string.zfile_txt),
                    ZFileContent.getStringById(context, R.string.zfile_other)};

            list = new ArrayList<>();
            list.add(ZFileQWFragment.newInstance(type, ZFileContent.ZFILE_QW_PIC, isManger));
            list.add(ZFileQWFragment.newInstance(type, ZFileContent.ZFILE_QW_MEDIA, isManger));
            list.add(ZFileQWFragment.newInstance(type, ZFileContent.ZFILE_QW_DOCUMENT, isManger));
            list.add(ZFileQWFragment.newInstance(type, ZFileContent.ZFILE_QW_OTHER, isManger));
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            ZFileListener.QWFileLoadListener qwFileLoadListener = ZFileContent.getZFileHelp().getQWFileLoadListener();
            if(qwFileLoadListener != null && qwFileLoadListener.getTitles() != null) {
                return qwFileLoadListener.getTitles()[position];
            }else {
                return titles[position];
            }
        }
    }
}
