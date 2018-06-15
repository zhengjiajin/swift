/*
 * @(#)ThreadPoolFactory.java   1.0  2018年6月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.swift.core.filter.ResponseFilter;
import com.swift.core.model.ServiceResponse;
import com.swift.util.queue.LimitQueue;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月14日
 */
@Component
public class ThreadPoolFactory implements ResponseFilter{
    
    //线程分类个数
    private static final int POOL_NUM=3;
    //分阶梯秒数
    private static final int EXE_TIME_SECOND=5;
    
    private static final int METHOD_NUM=3;
    //线程池
    private Map<Integer,ThreadPoolExecutorImpl> poolMap = new ConcurrentHashMap<Integer,ThreadPoolExecutorImpl>();
    //时间计算器(最近3次访问平均时间)
    private Map<String,LimitQueue<Long>> methodTime  = new ConcurrentHashMap<String,LimitQueue<Long>>();
    
    @PostConstruct
    public void init() {
        for(int i=0;i<POOL_NUM;i++) {
            poolMap.put(i, new ThreadPoolExecutorImpl());
        }
    }
    
    public void execute(ServerSendControl command) {
        String method = command.getReq().getMethod();
        LimitQueue<Long> longQueue = methodTime.get(method);
        int poolMuth = muthNum(longQueue);
        int poolNum =poolMuth<POOL_NUM-1?poolMuth:POOL_NUM-1;
        poolMap.get(poolNum).execute(command);
    }

    private int muthNum(LimitQueue<Long> longQueue) {
        if(longQueue==null || longQueue.size()<=0) return 0;
        long totalTime = 0;
        for(Long l:longQueue) {
            totalTime=totalTime+l;
        }
        return TypeUtil.toInt(totalTime/(EXE_TIME_SECOND*1000), 0);
    }
    
    /** 
     * @see com.swift.core.filter.ResponseFilter#doFilter(com.swift.core.model.ServiceResponse)
     */
    @Override
    public void doFilter(ServiceResponse res) {
        String method = res.getRequest().getMethod();
        long time = res.getResponseTime()-res.getRequest().getRequestTime();
        if(methodTime.get(method)==null) {
            methodTime.put(method, new LimitQueue<Long>(METHOD_NUM));
        }
        methodTime.get(method).add(time);
    }
}
