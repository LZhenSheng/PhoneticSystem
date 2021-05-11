package com.example.phoneticsystem.bmob;

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

    @Override
    public String toString() {
        return "Account{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
