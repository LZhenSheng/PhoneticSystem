package com.example.phoneticsystem.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

import es.dmoral.toasty.Toasty;

/***
* Toast工具类
* @author 胜利镇
* @time 2021/3/15
* @dec 
*/
public class ToastUtil {

    /**
     * 上下文
     */
    private static Context context;

    /**
     * 初始化方法
     *
     * @param context
     */
    public static void init(Context context) {
        ToastUtil.context = context;
    }

    /**
     * 显示短时间错误toast
     *
     * @param id
     */
    public static void errorShortToast(@StringRes int id) {
        Toasty.error(context, id, Toasty.LENGTH_SHORT).show();
    }

    /**
     * 显示短时间错误toast
     *
     * @param message
     */
    public static void errorShortToast(String message) {
        Toasty.error(context, message, Toasty.LENGTH_SHORT).show();
    }


    /**
     * 显示短时间正确toast
     *
     * @param id
     */
    public static void successShortToast(@StringRes int id) {
        Toasty.success(context, id, Toasty.LENGTH_SHORT).show();
    }

    /**
     * 显示短时间正确toast
     *
     * @param data
     */
    public static void successShortToast(String data) {
        Toasty.success(context, data, Toasty.LENGTH_SHORT).show();
    }

}
