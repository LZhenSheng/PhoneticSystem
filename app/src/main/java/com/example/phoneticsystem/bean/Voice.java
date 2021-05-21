package com.example.phoneticsystem.bean;

public class Voice {

    public float time;

    public String fileName;

    public String date;

    public boolean isMy;

    public boolean isRead;

    public Voice(String fileName,String data,boolean isMy,float time,boolean isRead){
        this.fileName=fileName;
        this.date=data;
        this.isMy=isMy;
        this.time=time;
        this.isRead=isRead;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
