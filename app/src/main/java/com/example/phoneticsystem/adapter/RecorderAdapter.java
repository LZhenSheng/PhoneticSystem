package com.example.phoneticsystem.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.ChatActivity.Recorder;
import com.example.phoneticsystem.bean.MessageStack;
import com.example.phoneticsystem.bean.Voice;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.TimeUtil;
import com.example.phoneticsystem.view.MediaManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class RecorderAdapter extends BaseQuickAdapter<Voice, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public RecorderAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     *
     * @param helper
     * @param data
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Voice data) {
        if(data.isMy()){
            helper.getView(R.id.left).setVisibility(View.GONE);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            helper.setText(R.id.time_right, TimeUtil.commonFormat(data.getDate()));

            //item 设定最小最大值
            int mMaxIItemWidth = (int) (outMetrics.widthPixels * 0.7f);
            int mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
            helper.setText(R.id.id_recorder_time_right,Math.round(data.getTime())+"\"");
            helper.getView(R.id.id_recorder_length_right).getLayoutParams().width=(int) (mMinItemWidth + (mMaxIItemWidth / 60f*data.time));
            //播放动画
            View mAnimView = helper.getView(R.id.id_recorder_anim_right);

            helper.getView(R.id.id_recorder_length_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAnimView.setBackgroundResource(R.drawable.play_anim);
                    AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
                    animation.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(data.getFileName(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mAnimView.setBackgroundResource(R.drawable.adj);
                        }
                    });
                }
            });
        }else{
            helper.getView(R.id.right).setVisibility(View.GONE);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            helper.setText(R.id.time_left, TimeUtil.commonFormat(data.getDate()));
            //item 设定最小最大值
            int mMaxIItemWidth = (int) (outMetrics.widthPixels * 0.7f);
            int mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
            helper.setText(R.id.id_recorder_time_left,Math.round(data.time)+"\"");
            helper.getView(R.id.id_recorder_length_left).getLayoutParams().width=(int) (mMinItemWidth + (mMaxIItemWidth / 60f*data.time));
            //播放动画
            View mAnimView = helper.getView(R.id.id_recorder_anim_left);

            helper.getView(R.id.id_recorder_length_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAnimView.setBackgroundResource(R.drawable.play_anim_2);
                    AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
                    animation.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(data.getFileName(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mAnimView.setBackgroundResource(R.drawable.adj_2);
                        }
                    });
                }
            });
        }
    }

}
