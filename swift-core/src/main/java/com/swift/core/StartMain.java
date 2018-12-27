/*
 * @(#)StartMain.java   1.0  2018年6月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.env.EnvLoader;
import com.swift.core.server.LifeCycle;
import com.swift.core.spring.Spring;
import com.swift.util.exec.ThreadUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月1日
 */
public class StartMain {
    private final static Logger log = LoggerFactory.getLogger(StartMain.class);

    public static String ENV="";
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            int port = 80;
            String env="";
            if(args!=null && args.length>0) {
                for(String arg:args) {
                    if(TypeUtil.isNumber(arg)) {
                        port=TypeUtil.toInt(arg);
                    }else {
                        env=arg;
                    }
                }
            }
            StartMain.ENV=EnvLoader.getEnv(env);
            log.info("starting...ENV:"+StartMain.ENV);
            for (String str : Spring.getAllBeanName()) {
                log.info("spring存在BEAN..." + str);
            }
            List<LifeCycle> servers = Spring.getBeans(LifeCycle.class);
            if (TypeUtil.isNotNull(servers)) {
                for (LifeCycle s : servers) {
                    log.info("准备启动服务:" + s+";port:"+port);
                    s.start(port); 
                    log.info("结束启动服务:" + s);
                }
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {  
                @Override  
                public void run() {  
                    if (TypeUtil.isNotNull(servers)) {
                        for (LifeCycle s : servers) {
                            log.info("准备停止服务:" + s);
                            s.stop();
                            log.info("结束停止服务:" + s);
                        }
                    }
                    ThreadUtil.sleep(30000);
                }  
            });  
        } catch (Exception ex) {
            log.error("主进程启动失败", ex);
            ThreadUtil.sleep(1000);
            System.exit(1);
        }
    }
}
