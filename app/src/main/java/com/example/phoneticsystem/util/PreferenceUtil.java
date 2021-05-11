package com.example.phoneticsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 偏好设计工具类
 * 是否登录了，是否显示引导界面，用户Id
 */
public class PreferenceUtil {

    /**
     * 实例
     * 单例
     */
    private static PreferenceUtil instance;

    /**
     * 偏好设置文件名称
     */
    private static final String NAME = "CHARITABLE";

    /**
     * 偏好设置文件名称
     */
    private static final String STATUE = "STATUE";

    /**
     * 偏好设置文件名称
     */
    private static final String USER_ID = "USER_ID";

    /**
     * 偏好设置文件名称
     */
    private static final String ACCOUNT = "ACCOUNT";

    /**
     * 偏好设置文件名称
     */
    private static final String PASSWORD = "PASSWORD";

    /**
     * 偏好设置文件名称
     */
    private static final String GUIDE = "GUIDE";

    /**
     * 上下文
     */
    private final Context context;
    private static SharedPreferences preference;

    /**
     * 构造方法
     * @param context
     */
    public PreferenceUtil(Context context) {
        //保存上下文
        this.context=context.getApplicationContext();

        //这样写有内存泄漏
        //因为当前工具类不会马上释放
        //如果当前工具类引用了界面实例
        //当界面关闭后
        //因为界面对应在这里还有引用
        //所以会导致界面对象不会被释放
        //this.context = context;

        //获取偏好设置
        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取偏好设置单例
     * @param context
     * @return
     */
    public static PreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance=new PreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 保存字符串
     *
     * @param key
     * @param value
     */
    private static void putString(String key, String value) {
        preference.edit().putString(key, value).commit();
    }

    /***
     * 获取字符串
     */
    private static String getString(String key) {
        return preference.getString(key,null);
    }
    /**
     * 保存boolean
     *
     * @param key
     * @param value
     */
    private static void putBoolean(String key, boolean value) {
        preference.edit().putBoolean(key, value).commit();
    }

    /***
     * 获取boolean
     * @param key
     * @return
     */
    private static boolean getBoolean(String key) {
        return preference.getBoolean(key, false);
    }

    /**
     * 删除内容
     *
     * @param key
     */
    private static void delete(String key) {
        preference.edit().remove(key).commit();
    }

    /***
     * 获取一个int
     * @param key
     * @return
     */
    private static int getInt(String key) {
        return preference.getInt(key, 0);
    }

    /**
     * 设置一个int
     *
     * @param key
     * @param data
     */
    private static void putInt(String key, int data) {
        preference.edit().putInt(key, data).apply();
    }

    /***
     * 设置用户ID
     */
    public static void setGuide(boolean data) {
        putBoolean(GUIDE,data);
    }
    /**
     * 获取登录状态
     */
    public static boolean getGuide() {
        return getBoolean(GUIDE);
    }

    /***
     * 设置用户ID
     */
    public static void setUserId(String data) {
        putString(USER_ID,data);
    }
    /**
     * 获取登录状态
     */
    public static String getUserId() {
        return getString(USER_ID);
    }

    /***
     * 设置用户ID
     */
    public static void setAccount(String data) {
        putString(ACCOUNT,data);
    }
    /**
     * 获取登录状态
     */
    public static String getAccount() {
        return getString(ACCOUNT);
    }


    /***
     * 设置用户ID
     */
    public static void setPassword(String data) {
        putString(PASSWORD,data);
    }
    /**
     * 获取登录状态
     */
    public static String getPassword() {
        return getString(PASSWORD);
    }

    /***
     * 设置用户ID
     */
    public static void setStatue(Boolean data) {
        putBoolean(STATUE,data);
    }
    /**
     * 获取登录状态
     */
    public static Boolean getStatue() {
        return getBoolean(STATUE);
    }
}
