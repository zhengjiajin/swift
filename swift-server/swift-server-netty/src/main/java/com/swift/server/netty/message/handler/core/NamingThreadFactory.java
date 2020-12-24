/**
 * NamingThreadFactory.java 1.0.0 2018-10-17 23:47:14
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;

/**
 * 可定义线程名称的线程工厂类。
 * 
 * @version 1.0.0
 * @date 2018-10-17 23:47:14
 */
public class NamingThreadFactory implements ThreadFactory {

    private static final Map<String, AtomicInteger> THREAD_NUMBERS = new ConcurrentHashMap<String, AtomicInteger>();

    private String name;

    public NamingThreadFactory(String name) {
        if (StringUtils.isBlank(name)) {
            name = "DefaultThread";
        }
        this.name = name;
        THREAD_NUMBERS.putIfAbsent(name, new AtomicInteger(0));
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, threadName());
    }

    private String threadName() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("-");
        builder.append(THREAD_NUMBERS.get(name).getAndIncrement());
        return builder.toString();
    }
}
