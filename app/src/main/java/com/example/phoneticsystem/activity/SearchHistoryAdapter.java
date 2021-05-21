package com.example.phoneticsystem.activity;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.phoneticsystem.R;
import com.example.phoneticsystem.bmob.Account;
import com.example.phoneticsystem.util.PreferenceUtil;

/**
 * 搜索历史适配器
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<Account, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public SearchHistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 绑定数据
     *
     * @param helper
     * @param data
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Account data) {
        //标题
        helper.setText(R.id.tv_title, data.getAccount());
    }
}
