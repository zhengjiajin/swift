/*
 * @(#)AuthorizeCodeFactory.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message.code;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.swift.util.exec.ThreadUtil;

/**
 * 创建共享授权的CODE工厂
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
@Component
public class AuthorizeCodeFactory implements Runnable  {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorizeCodeFactory.class);
    
    private Map<String, AuthorizeCode> codeMap = new ConcurrentHashMap<>();
    
    // 超时时间
    private final static int TIMEOUT=60;
    private AtomicBoolean isTerminated = new AtomicBoolean(false);
    private Thread t;
    
    @PostConstruct
    private void init() {
        t = new Thread(this);
        t.setName(AuthorizeCodeFactory.class.getSimpleName());
        t.setDaemon(true);
        t.start();
    }

    @PreDestroy
    private void destroy() {
        isTerminated.set(true);
        if (t != null) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
    }
    
    @Override
    public void run() {
        while (!isTerminated.get()) {
            try {
                for(Entry<String, AuthorizeCode> entry: codeMap.entrySet()) {
                    if (entry.getValue() == null) continue;
                    if (System.currentTimeMillis() - entry.getValue().getTimestamp() < TIMEOUT*1000) {
                        continue;
                    }
                    codeMap.remove(entry.getKey());
                }
            } catch (Throwable ex) {
                logger.error("Unexpected exception: ", ex);
            } finally {
                ThreadUtil.sleep(1000);
            }
        }
    }
    
    /**
     * 根据随机授权码获取授权码信息。
     * 
     * @param secSocketAccept
     *            随机授权码
     * @return 授权码信息
     */
    public AuthorizeCode get(String secSocketAccept) {
        return codeMap.remove(secSocketAccept);
    }

    /**
     * 保存随机授权码对应的授权码信息。
     * 
     * @param secSocketAccept
     *            随机授权码
     * @param value
     *            授权码信息
     */
    public void set(String secSocketAccept, AuthorizeCode authorizeCode) {
        if(authorizeCode.getTimestamp()==0)authorizeCode.setTimestamp(System.currentTimeMillis());
        codeMap.put(secSocketAccept, authorizeCode);
    }
}
