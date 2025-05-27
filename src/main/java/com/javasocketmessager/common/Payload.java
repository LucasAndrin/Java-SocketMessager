package com.javasocketmessager.common;

import com.google.gson.Gson;

public class Payload {

    private PayloadType type;

    private User from;

    private String content;

    public Payload() {

    }

    public PayloadType type() {
        return this.type;
    }

    public Payload type(PayloadType type) {
        this.type = type;
        return this;
    }

    public User from() {
        return this.from;
    }

    public Payload from(User from) {
        this.from = from;
        return this;
    }

    public String content() {
        return this.content;
    }

    public Payload content(String content) {
        this.content = content;
        return this;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
