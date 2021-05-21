package com.example.phoneticsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseActivity;
import com.example.phoneticsystem.adapter.MainAdapter;
import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.bean.MessageStack;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nekocode.badge.BadgeDrawable;

public class MainActivity extends BaseActivity {

    ImageView iv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_count=findViewById(R.id.iv_count);
        showMessageCount();
    }

    private static final String TAG = "MainActivity";
    @BindView(R.id.vp)
    ViewPager vp;

    MainAdapter adapter;

    @BindView(R.id.indicator_iv1)
    ImageView indicator_iv1;
    @BindView(R.id.indicator_iv4)
    ImageView indicator_iv4;

    @OnClick({R.id.indicator_iv1,R.id.indicator_iv4})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.indicator_iv1:
                clearBackground();
                indicator_iv1.setImageResource(R.mipmap.home_selected);
                vp.setCurrentItem(0);
                break;
            case R.id.indicator_iv4:
                clearBackground();
                indicator_iv4.setImageResource(R.mipmap.my_selected);
                vp.setCurrentItem(1);
                break;
        }
    }

    private void clearBackground() {
        indicator_iv1.setImageResource(R.mipmap.home);
        indicator_iv4.setImageResource(R.mipmap.my);
    }

    @Override
    public void initData() {
        super.initData();
        //缓存页面数量 默认是缓存一个
        vp.setOffscreenPageLimit(3);

        //主界面页面MainAda
        adapter = new MainAdapter(getMainActivity(), getSupportFragmentManager());
        vp.setAdapter(adapter);

        ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(2);
        adapter.setDatum(datas);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clearBackground();
                switch (position) {
                    case 0:
                        indicator_iv1.setImageResource(R.mipmap.home_selected);
                        break;
                    case 1:
                        indicator_iv4.setImageResource(R.mipmap.my_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }

    /**
     * 显示消息未读数
     */
    private void showMessageCount() {
        Gson gson=new Gson();
        int count=0;
        List<MessageStack> list=gson.fromJson(PreferenceUtil.getMessage(),new TypeToken<List<MessageStack>>(){}.getType());
        if(list!=null){
            for(int i=0;i<list.size();i++){
                count+=list.get(i).getMessages().size();
            }
        }
        if (count > 0) {
            //有未读消息

            //我的消息未读消息数drawable
            BadgeDrawable countDrawable = new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_NUMBER)
                    .number(count)

                    //设置背景颜色
                    //这里使用了兼容方法获取颜色
                    .badgeColor(ContextCompat.getColor(getMainActivity(), R.color.main_color))
                    .build();

            iv_count.setImageDrawable(countDrawable);
        } else {
            //没有未读消息
            iv_count.setImageDrawable(null);
        }
    }
}
