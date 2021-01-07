package com.kathline.library.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.kathline.library.content.ZFileContent;

public class ProxyFragment extends Fragment {

    private FragmentActivity mActivity;
    public static String REQUEST_CODE = "request_code";
    private ProxyListener mListener;
    private int requestCode = 0;

    public ProxyFragment() {

    }

    /**
     * 开启权限申请
     */
    public static ProxyFragment beginRequest(FragmentActivity activity, int requestCode, ProxyListener listener) {
        ProxyFragment fragment = new ProxyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, requestCode);
        fragment.setArguments(bundle);
        // 设置保留实例，不会因为配置变化而重新创建
        fragment.setRetainInstance(true);
        // 设置权限回调监听
        fragment.setProxyListener(listener);
        addFragment(activity.getSupportFragmentManager(), fragment);
        return fragment;
    }

    /**
     * 开启权限申请
     */
    public static ProxyFragment beginRequest(Fragment f, int requestCode, ProxyListener listener) {
        ProxyFragment fragment = new ProxyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_CODE, requestCode);
        fragment.setArguments(bundle);
        // 设置保留实例，不会因为配置变化而重新创建
        fragment.setRetainInstance(true);
        // 设置权限回调监听
        fragment.setProxyListener(listener);
        addFragment(f.getChildFragmentManager(), fragment);
        return fragment;
    }

    private void setProxyListener(ProxyListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            requestCode = getArguments().getInt(REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListener != null) {
            mListener.onResult(requestCode, resultCode, data);
        }else {
            if(getActivity() != null) {
                getActivity().setResult(ZFileContent.ZFILE_REQUEST_CODE, data);
            }
        }
        // 将 Fragment 从 Activity 移除
        removeFragment(getFragmentManager(), this);
    }

    /**
     * 添加 Fragment
     */
    public static void addFragment(FragmentManager manager, Fragment fragment) {
        if (manager == null) {
            return;
        }
        manager.beginTransaction().add(fragment, fragment.toString()).commitNowAllowingStateLoss();
    }

    /**
     * 移除 Fragment
     */
    public static void removeFragment(FragmentManager manager, Fragment fragment) {
        if (manager == null) {
            return;
        }
        manager.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }
}
