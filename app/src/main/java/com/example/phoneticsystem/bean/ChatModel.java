package com.example.phoneticsystem.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChatModel {

    private String account;

    private List<Voice> voices;

    public ChatModel(){
        voices=new ArrayList<>();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<Voice> getVoices() {
        return voices;
    }

    public void setVoices(List<Voice> voices) {
        this.voices = voices;
    }

    public void addVoices(Voice voice) {
        this.voices.add(voice);
    }
}
