package com.example.phoneticsystem.util;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 数据模型层全局类
public class ThreadUtil {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();

    // 创建对象
    private static ThreadUtil model = new ThreadUtil();

    // 私有化构造
    private ThreadUtil() {

    }

    // 获取单例对象
    public static ThreadUtil getInstance(){

        return model;
    }

    // 初始化的方法
    public void init(Context context){
        mContext = context;
    }

    // 获取全局线程池对象
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }
}