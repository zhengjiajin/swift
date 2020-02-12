/*
 * @(#)ShardingTable.java   1.0  2020年2月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource.sharding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识使用Sharding的数据表,库描述规则固定为${0..N} ,固定归为配置JDBC连接,号的顺序
 * @author zhengjiajin
 * @version 1.0 2020年2月6日
 */
@Target(value= {ElementType.TYPE,ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ShardingTable {
    /*
     * 逻辑表名
     */
    public String table();
    /*
     * 自定义分库分表策略,需要实现ShardingRuleInterface
     */
    public Class<?> shardingClass() default ShardingRuleInterface.class;
    /*
     * 是否所有分片表，则所有库都存在，更新时会更新所有库
     */
    public boolean isBroadcastTables()  default false;
    /*
     * broadcastTable 所在的数据标识识，对应的配置名称
     */
    public String broadcastTablesDb() default "";
}
