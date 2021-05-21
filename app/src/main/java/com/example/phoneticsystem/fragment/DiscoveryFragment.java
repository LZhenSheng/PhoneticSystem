package com.example.phoneticsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseActivity;

/***
 * 发现界面
 */
public class DiscoveryFragment extends BaseTitleFragment {

    /**
     * 构造方法
     * <p>
     * 固定写法
     *
     * @return
     */
    public static DiscoveryFragment newInstance() {
        Bundle args = new Bundle();

        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局文件
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, null);
    }

    @Override
    protected void initData() {
        super.initData();
        lightStatusBar(R.color.main_color);
        toolbar.setTitle("好友");
        setTitleCenter(toolbar);
    }
}