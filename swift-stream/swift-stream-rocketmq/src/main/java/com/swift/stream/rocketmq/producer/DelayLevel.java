/*
 * @(#)DelayLevel.java   1.0  2021年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2021年7月2日
 */
public enum DelayLevel {
    LEVEL_NOT(0,0),
    LEVEL_1s(1,1*1000), 
    LEVEL_5s(2,5*1000), 
    LEVEL_10s(3,10*1000), 
    LEVEL_30s(4,30*1000), 
    LEVEL_1m(5,60*1000), 
    LEVEL_2m(6,2*60*1000), 
    LEVEL_3m(7,3*60*1000), 
    LEVEL_4m(8,4*60*1000), 
    LEVEL_5m(9,5*60*1000), 
    LEVEL_6m(10,6*60*1000), 
    LEVEL_7m(11,7*60*1000), 
    LEVEL_8m(12,8*60*1000), 
    LEVEL_9m(13,9*60*1000), 
    LEVEL_10m(14,10*60*1000), 
    LEVEL_20m(15,20*60*1000), 
    LEVEL_30m(16,30*60*1000), 
    LEVEL_1h(17,3600*1000), 
    LEVEL_2h(18,2*3600*1000);
    
    private int level;
    
    private int lazyTime;
    
    private DelayLevel(int level,int lazyTime) {
        this.level=level;
        this.lazyTime=lazyTime;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the lazyTime
     */
    public int getLazyTime() {
        return lazyTime;
    }
    
}
