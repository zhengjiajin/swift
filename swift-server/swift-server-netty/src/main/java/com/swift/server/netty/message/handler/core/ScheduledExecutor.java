/**
 * @(#)ScheduleEventExecutorGroup.java 0.0.1-SNAPSHOT Dec 26, 2015 1:31:56 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.netty.message.handler.core;

import java.util.concurrent.ThreadFactory;

import org.springframework.stereotype.Component;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * Scheduled executor
 * 
 * @date Dec 26, 2015 1:31:56 PM
 */
@Component
public class ScheduledExecutor extends DefaultEventExecutorGroup {

    public ScheduledExecutor() {
        this(10);
    }

    public ScheduledExecutor(int nThreads) {
        super(nThreads, new NamingThreadFactory("ScheduledExecutor"));
    }

    public ScheduledExecutor(int nThreads, ThreadFactory threadFactory) {
        super(nThreads, threadFactory);
    }
}
