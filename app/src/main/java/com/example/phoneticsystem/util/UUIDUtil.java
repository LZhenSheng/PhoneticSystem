package com.example.phoneticsystem.util;

import android.os.Build;

import java.util.UUID;

/**
 * 安卓设备唯一码工具类
 * @author xudongdong
 *
 */
public class UUIDUtil {

    /***
     * 获取手机唯一的设备ID
     * @return
     */
    public static String getUniquePsuedoID() {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    //    生成UUID唯一标示符
//    算法的核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID
//    .amr音频文件
    public static String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }
}
