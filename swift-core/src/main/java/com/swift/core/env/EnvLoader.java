/*
 * @(#)EnvLoader.java   1.0  2018年12月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.exception.SwiftRuntimeException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年12月26日
 */
public class EnvLoader {
    private final static Logger log = LoggerFactory.getLogger(EnvLoader.class);
    //配置环境的KEY名称
    private static final String ENV_KEY="APP_ENV_KEY";
    //当前环境
    private static Env NOW=null;
    //初始化开发环境
    public static void loadEnv(String env) {
        //传入参数优先
        if(TypeUtil.isNotNull(env)) {
            log.info("从输入参数里得到配置:"+env);
            EnvLoader.NOW=Env.getEnvByPath(env);
        }
        if(NOW==null) {
            //系统参数
            env = System.getProperty(ENV_KEY);
            if(TypeUtil.isNotNull(env)) {
                log.info("从系统参数里得到配置:"+env);
                EnvLoader.NOW=Env.getEnvByPath(env);
            }
        }
        if(NOW==null) {
            //环境变量
            env = System.getenv(ENV_KEY);
            if(TypeUtil.isNotNull(env)) {
                log.info("从环境变量里得到配置:"+env);
                EnvLoader.NOW=Env.getEnvByPath(env);
            }
        }
        log.info("未配置环境参数,默认为开发环境,将取根目录资源环境");
        EnvLoader.NOW=Env.DEV;
    }
    /**
     * 空为开发环境，test为测试，prod为生产
     * @param evn
     * @return
     */
    public static Env getEnv(){
        if(EnvLoader.NOW==null) throw new SwiftRuntimeException("环境配置没有初始化,请先初始化环境");
        return EnvLoader.NOW;
    }
}
