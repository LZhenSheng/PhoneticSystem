package com.example.phoneticsystem.util;

import org.joda.time.DateTime;


public class DateUtil {
    public static String getDate(){
        return new DateTime().toString();
    }
}
