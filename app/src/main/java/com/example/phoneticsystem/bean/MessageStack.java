package com.example.phoneticsystem.bean;

import java.util.ArrayList;
import java.util.List;

public class MessageStack {

    private String account;

    private List<String> messages;

    public MessageStack(){
        messages=new ArrayList<>();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessages(String messages) {
        this.messages.add(messages);
    }
}
