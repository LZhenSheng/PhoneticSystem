package com.example.phoneticsystem.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.phoneticsystem.R;
import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.bean.MessageStack;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<ChatModel, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public MessageAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     *
     * @param helper
     * @param data
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatModel data) {
        helper.setText(R.id.tv_name, data.getAccount());
        Gson gson = new Gson();
        List<MessageStack> list = gson.fromJson(PreferenceUtil.getMessage(), new TypeToken<List<MessageStack>>() {
        }.getType());
        helper.setVisible(R.id.tv_count,false);
        if(list!=null){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getAccount().equals(data.getAccount())&&list.get(i).getMessages().size()>0) {
                    helper.setVisible(R.id.tv_count, true);
                    helper.setText(R.id.tv_count, String.valueOf(list.get(i).getMessages().size()));
                    break;
                }
            }
        }
        if (data.getVoices().size() > 0) {
            helper.setText(R.id.tv_time, TimeUtil.commonFormat(data.getVoices().get(data.getVoices().size() - 1).getDate()));
        }
    }

}
