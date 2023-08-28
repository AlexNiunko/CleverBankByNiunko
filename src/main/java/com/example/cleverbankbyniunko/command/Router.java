package com.example.cleverbankbyniunko.command;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.net.Proxy;

public class Router {
    @Getter
    @Setter
    private String page = PagePath.INDEX;
    @Getter
    private Type type = Type.FORWARD;


    public enum Type {
        FORWARD, REDIRECT
    }

    public Router() {
    }

    public Router(String page, Type type) {
        this.page = page;
        this.type = type;
    }
    public void setRedirect(){this.type=Type.REDIRECT;}
}
