/*
 * @(#)ShardingRuleInterface.java   1.0  2020年2月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;

import com.swift.dao.db.datasource.sharding.core.DefaultRangeSharding;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年2月11日
 */
public interface ShardingRuleInterface {
    /*
     * 分区分表关键字段，只支持单字段
     */
    public String tableShardingColumn();
    /*
     * 分库关键字段，只支持单字段
     */
    public String dbShardingColumn();
    /*
     * 分表描述规则现,如master${0..1}.t_order_${0..1}
     */
    public String rule();
    /*
     * 自定义分库策略,以配置的jdbc.properties为前序,后续为URL配置的顺序
     * 精准匹配,适用于IN,=等操作
     */
    public PreciseShardingAlgorithm<?> dbPreciseSharding();
    /*
     * 自定义分表策略
     * 精确分片算法，用于=和IN
     */
    public PreciseShardingAlgorithm<?> tablePreciseSharding();
    /*
     * 自定义分库策略,以配置的jdbc.properties为前序,后续为URL配置的顺序
     * 范围分片算法，用于BETWEEN
     */
    public default RangeShardingAlgorithm<?> dbRangeSharding(){
        return new DefaultRangeSharding();
    }
    /*
     * 自定义分表策略
     * 范围分片算法，用于BETWEEN
     */
    public default RangeShardingAlgorithm<?> tableRangeSharding(){
        return new DefaultRangeSharding();
    }
    
}
