package com.javasocketmessager.common;

public class User {
    private String name;

    public User() {

    }

    public String name() {
        return name;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

}
