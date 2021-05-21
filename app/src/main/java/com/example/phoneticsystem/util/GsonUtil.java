package com.example.phoneticsystem.util;

import com.example.phoneticsystem.liteorm.User;
import com.google.gson.Gson;

public class GsonUtil {

    public static String toJson(User user){
        Gson gson=new Gson();
        return gson.toJson(user);
    }

    public static User fromJsonUser(String content) {
        Gson gson = new Gson();
        User user = gson.fromJson(content, User.class);
        return user;
    }
}
