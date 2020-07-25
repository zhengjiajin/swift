/*
 * @(#)ServerThreadPoolExecutor.java   1.0  2014-12-4
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.exception.extend.SystemException;


/**
 * 用于优先级控制的线程池 最大固定大小，用不完能自动回收，超过时放在队列控制优先级
 * 
 * @author jiajin
 * @version 1.0 2014-12-4
 */
public class ThreadPoolExecutorImpl {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorImpl.class);
    private int maxPoolSize = 300;
    /**
     * 系统线程池
     */
    private ThreadPoolExecutor executorService;
    /**
     * 优先级队列PriorityBlockingQueue
     */
    private ArrayBlockingQueue<ThreadPoolChannel> queue = new ArrayBlockingQueue<ThreadPoolChannel>(1000);

    private int corePoolSize = 0;

    public ThreadPoolExecutorImpl() {
        executorService = new ThreadPoolExecutor(corePoolSize(), maxPoolSize,60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        executorService.setRejectedExecutionHandler(new ExecutionHandler());
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
    public void execute(Runnable command) {
        ThreadPoolChannel exe = new ThreadPoolChannel(command);
        executorService.execute(exe);
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

    private class ThreadPoolChannel implements Runnable {

        private Runnable command;

        private ThreadPoolChannel(Runnable command) {
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
        }
       
    }
    
    private class  ExecutionHandler implements RejectedExecutionHandler{

        /** 
         * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            boolean queueCheck = queue.offer((ThreadPoolChannel)r);
            if(queueCheck) log.warn("线程池已满，请注意现队列长度为："+queue.size()+";内容为:"+getRunThread());
            if(!queueCheck) throw new SystemException("系统忙，请稍后再试!");
        }
        
    }

}
