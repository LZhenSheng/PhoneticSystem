package com.example.phoneticsystem.util;

import static com.example.phoneticsystem.util.Constant.REGEX_PHONE;

/**
 * 字符串相关工具类
 */
public class StringUtil {

    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        if(value==null)
            return false;
        return value.matches(REGEX_PHONE);
    }
}
