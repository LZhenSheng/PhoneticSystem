package com.example.phoneticsystem.util;

import android.util.Log;

import com.example.phoneticsystem.BuildConfig;

/**
 * 日志工具类
 * 对Android日志API做一层简单的封装
 */
public class LogUtil {

    /**
     * 是否是调试模式
     */
    public static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 调试级别日志
     * @param tag
     * @param value
     */
    public static void d(String tag, String value) {
        if (isDebug) {
            Log.d(tag,value);
        }
    }
}
