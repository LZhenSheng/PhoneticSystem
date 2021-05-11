package com.example.phoneticsystem.context;

import android.app.Application;

import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.ToastUtil;

import cn.bmob.v3.Bmob;
import es.dmoral.toasty.Toasty;

/***
 * 全局配置文件
 * @author 胜利镇
 * @time 2020/8/6 22:45
 */
public class AppContext extends Application {


    /**
     * 偏好设置
     * 存储离线数据和特殊标记位
     */
    private PreferenceUtil sp;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Toast工具类
        Toasty.Config.getInstance().apply();

        //初始化toast工具类
        ToastUtil.init(getApplicationContext());

        //偏好设置初始化
        sp=PreferenceUtil.getInstance(getApplicationContext());

        Bmob.initialize(this,"ef93335c2474e133efcd557c631576d1");
    }

}