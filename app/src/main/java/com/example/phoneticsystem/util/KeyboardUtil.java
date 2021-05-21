package com.example.phoneticsystem.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘相关工具类
 */
public class KeyboardUtil {
    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        //获取输入管理器
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        //隐藏软键盘
        inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus()
                        .getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
