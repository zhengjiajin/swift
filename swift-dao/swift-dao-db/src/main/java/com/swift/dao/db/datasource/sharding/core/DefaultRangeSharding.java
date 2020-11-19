/*
 * @(#)IntegerRangeSharding.java   1.0  2020年2月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource.sharding.core;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年2月11日
 */
public class DefaultRangeSharding implements RangeShardingAlgorithm<Integer>{
   
    /** 
     * @see org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm#doSharding(java.util.Collection, org.apache.shardingsphere.api.sharding.standard.RangeShardingValue)
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,RangeShardingValue<Integer> shardingValue) {
        return availableTargetNames;
    }

}
