package com.example.phoneticsystem.bmob;

import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Account extends BmobObject {

    /***
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    private String ip;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDate() {
        Gson gson = new Gson();
        List<ChatModel> suggests = gson.fromJson(PreferenceUtil.getResotreAllRecord(), new TypeToken<List<ChatModel>>() {}.getType());
        for(int i=0;i<suggests.size();i++){
            if(suggests.get(i).getAccount().equals(account)){
                return suggests.get(i).getVoices().get(suggests.get(i).getVoices().size()).getDate();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
