/*
 * @(#)NumberShardingAlgorithm.java   1.0  2020年2月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource.sharding.core;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 通过INTEGER取余的方式处理分区分表 
 * @author zhengjiajin
 * @version 1.0 2020年2月11日
 */
public class IntegerPreciseSharding implements PreciseShardingAlgorithm<Integer> {
    //分区标识前序
    private String shardingSplt;
    //分区标识取余，余数
    private int mode;
    
    public IntegerPreciseSharding(String shardingSplt,int mode) {
        this.shardingSplt=shardingSplt;
        this.mode=mode;
    }
    /** 
     * @see org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm#doSharding(java.util.Collection, org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue)
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        if(shardingValue.getValue()==null) return null;
        return shardingSplt+(shardingValue.getValue() % mode);
    }

}
