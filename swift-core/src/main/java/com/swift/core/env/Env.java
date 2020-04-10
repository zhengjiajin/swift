/*
 * @(#)Env.java   1.0  2019年1月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.env;

import com.swift.util.type.TypeUtil;

/**
 * 环境enum 
 * @author zhengjiajin
 * @version 1.0 2019年1月21日
 */
public enum Env {
    
    DEV(""),TEST("test"),DEMO("demo"),PROD("prod");
    //环境目录
    private String path;
    
    private Env(String path) {
        this.path=path;
    }
    //不带/
    public String getPath() {
        return path;
    }
    //带前后/
    public String getEnvPath() {
        if(TypeUtil.isNull(path)) return "";
        return "/"+path+"/";
    }
    
    public static Env getEnvByPath(String path) {
        for(Env env:Env.values()) {
            if(env.getPath().equalsIgnoreCase(path)) {
                return env;
            }
        }
        return null;
    }
    
}
