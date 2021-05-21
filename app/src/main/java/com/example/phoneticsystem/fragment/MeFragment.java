package com.example.phoneticsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.LoginActivity;
import com.example.phoneticsystem.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页-我的界面
 */
public class MeFragment extends BaseTitleFragment {

    @OnClick(R.id.exit)
    public void onExit(){
        PreferenceUtil.setLogin(false);
        startActivityAfterFinishThis(LoginActivity.class);
    }

    /**
     * 构造方法
     *
     * 固定写法
     *
     * @return
     */
    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
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
        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    protected void initData() {
        super.initData();
        lightStatusBar(R.color.main_color);
        toolbar.setTitle("我的");
        setTitleCenter(toolbar);
    }
}