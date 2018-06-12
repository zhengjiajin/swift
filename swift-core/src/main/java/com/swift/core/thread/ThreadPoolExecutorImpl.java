/*
 * @(#)ServerThreadPoolExecutor.java   1.0  2014-12-4
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 用于优先级控制的线程池 最大固定大小，用不完能自动回收，超过时放在队列控制优先级
 * 
 * @author jiajin
 * @version 1.0 2014-12-4
 */
@Service
public class ThreadPoolExecutorImpl {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorImpl.class);
    private int maxPoolSize = 300;
    /**
     * 系统线程池
     */
    private ExecutorService executorService;
    /**
     * 优先级队列PriorityBlockingQueue
     */
    private PriorityBlockingQueue<ThreadPoolChannel> queue = new PriorityBlockingQueue<ThreadPoolChannel>();
    /**
     * 是否直接插入queue
     */
    private AtomicInteger queueCheck = new AtomicInteger(0);

    private int corePoolSize = 0;

    @PostConstruct
    public void init() {
        executorService = new ThreadPoolExecutor(corePoolSize(), maxPoolSize,
                60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    /**
     * @return
     */

    private int corePoolSize() {
        corePoolSize = (maxPoolSize * 10) / 7 + 1 - maxPoolSize;
        return corePoolSize;
    }

    /**
     * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
     */
    public void execute(ThreadPoolInterface command) {
        ThreadPoolChannel exe = new ThreadPoolChannel(command);
        if (queueCheck.get() >= maxPoolSize - corePoolSize) {
            log.warn("线程池已满，请注意现队列长度为："+queue.size()+";内容为:"+getRunThread());
            queue.offer(exe);
        } else {
            try {
                queueCheck.incrementAndGet();
                executorService.execute(exe);
            } catch (Throwable e) {
                log.error("主线程队列已满异常:"+e);
                queue.offer(exe);
                queueCheck.decrementAndGet();
            }
        }
    }
    
    private String getRunThread() {
        StringBuffer sb = new StringBuffer();
        try {
            ThreadGroup group = Thread.currentThread().getThreadGroup();  
            ThreadGroup topGroup = group;  
            while (group != null) {  
                topGroup = group;  
                group = group.getParent();  
            } 
            Thread[] slackList = new Thread[maxPoolSize*2];   
            int actualSize = topGroup.enumerate(slackList); 
            Thread[] list = new Thread[actualSize];  
            System.arraycopy(slackList, 0, list, 0, actualSize);  
            for (Thread thread : list) {  
                sb.append(thread.getId()+":"+thread.getName()+";");  
            }  
        }catch(Throwable ex) {
            log.error("线程名称获取错误",ex);
        }
        return sb.toString();
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    private class ThreadPoolChannel implements Runnable,Comparable<ThreadPoolChannel> {

        private ThreadPoolInterface command;

        private ThreadPoolChannel(ThreadPoolInterface command) {
            this.command = command;
        }

        @Override
        public void run() {
            command.run();
            ThreadPoolChannel qc = queue.poll();
            while (qc != null) {
                qc.command.run();
                qc = queue.poll();
            } 
            queueCheck.decrementAndGet();
        }
        /** 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(ThreadPoolChannel o) {
            if(command.getCompareTo()-o.command.getCompareTo()>0) return 1;
            if(command.getCompareTo()-o.command.getCompareTo()<0) return -1;
            return 0;
        }

    }

}
