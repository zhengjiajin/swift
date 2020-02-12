/*
 * @(#)ShardingConfig.java   1.0  2020年2月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource.sharding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import com.swift.exception.SwiftRuntimeException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年2月6日
 */
@Component("shardingConfig")
public class ShardingConfig {

    private static final String PACKAGE="com.swift";
    
    private ShardingRuleConfiguration shardingRuleConfig;
    //所有汲到的数据库描术名
    private List<String> ruleListRem = new CopyOnWriteArrayList<String>();
    
    public boolean isShardingDb(String source) {
        for(String rule:ruleListRem) {
            if(rule.startsWith(source)) return true;
        }
        return false;
    }
    
    
    public DataSource getShardingDataSource(Map<String, DataSource> dataSourceMap) {
        try {
            if(TypeUtil.isNull(dataSourceMap)) throw new SwiftRuntimeException("c3p0 DataSource is empty");
            return ShardingDataSourceFactory.createDataSource(dataSourceMap, createShardingRule(), new Properties());
        } catch (Exception e) {
            throw new SwiftRuntimeException("create ShardingDataSource error",e);
        }
    }
    
    @PostConstruct
    private  ShardingRuleConfiguration createShardingRule() {
        if(shardingRuleConfig!=null) return shardingRuleConfig;
        shardingRuleConfig = new ShardingRuleConfiguration();
        //shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        //shardingRuleConfig.setDefaultTableShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        //shardingRuleConfig.getBindingTableGroups().add(shardingTable.table());
        List<ShardingTable> shardingTableList = scan();
        if(TypeUtil.isNull(shardingTableList)) return shardingRuleConfig;
        for(ShardingTable shardingTable : shardingTableList) {
            if(shardingTable.isBroadcastTables()) {
                shardingRuleConfig.getBroadcastTables().add(shardingTable.table());
                ruleListRem.add(shardingTable.broadcastTablesDb());
            }else {
                TableRuleConfiguration ruleConfig = getTableRuleConfiguration(shardingTable);
                if(ruleConfig!=null) {
                    shardingRuleConfig.getTableRuleConfigs().add(ruleConfig);
                }
            }
        }
        return shardingRuleConfig;
    }
    
    private TableRuleConfiguration getTableRuleConfiguration(ShardingTable shardingTable) {
        ShardingRuleInterface rule = newRule(shardingTable.shardingClass());
        if(rule==null) return null;
        TableRuleConfiguration result = new TableRuleConfiguration(shardingTable.table(), rule.rule());
        ruleListRem.add(rule.rule());
        //result.setKeyGeneratorConfig(getKeyGeneratorConfiguration()); -- 主键生成器
        if(TypeUtil.isNotNull(rule.dbShardingColumn())) {
            if(rule.dbPreciseSharding()!=null) {
                ShardingStrategyConfiguration dbConfig = new StandardShardingStrategyConfiguration(rule.dbShardingColumn(),rule.dbPreciseSharding(),rule.dbRangeSharding());
                result.setDatabaseShardingStrategyConfig(dbConfig);
            }
        }
        if(TypeUtil.isNotNull(rule.tableShardingColumn())) {
            if(rule.tablePreciseSharding()!=null) {
                ShardingStrategyConfiguration tblaeConfig = new StandardShardingStrategyConfiguration(rule.tableShardingColumn(),rule.tablePreciseSharding(),rule.tableRangeSharding());
                if(tblaeConfig!=null) result.setTableShardingStrategyConfig(tblaeConfig);
            }
        }
        return result;
    }
    
    private ShardingRuleInterface newRule(Class<?> cla) {
        if(cla!=null && !cla.isInterface()) {
            try {
                Object obj = cla.newInstance();
                if(!(obj instanceof ShardingRuleInterface)) {
                    throw new SwiftRuntimeException(cla+" not implements ShardingRuleInterface");
                }
                return (ShardingRuleInterface)obj;
            } catch (Exception e) {
                throw new SwiftRuntimeException("create class is error",e);
            }
        }
        return null;
    }
    /*
     * 找到所有配置了注解的类
     */
    private List<ShardingTable> scan(){
        //找到加了注解的TABLE
        ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
        TypeFilter includeFilter = new AnnotationTypeFilter(ShardingTable.class);
        scan.addIncludeFilter(includeFilter);
        Set<BeanDefinition> set = scan.findCandidateComponents(PACKAGE);
        if(TypeUtil.isNull(set)) return null;
        List<ShardingTable> list = new ArrayList<>();
        for(BeanDefinition bean : set) {
            String beanClassName = bean.getBeanClassName();
            Class<?> cla = classForName(beanClassName);
            ShardingTable shardingTable = cla.getAnnotation(ShardingTable.class);
            if(!checkHasTable(list, shardingTable.table())) {
                list.add(shardingTable);
            }
        }
        return list;
    }
    /*
     * 去重
     */
    private boolean checkHasTable(List<ShardingTable> list,String table){
        if(TypeUtil.isNull(list)) return false;
        for(ShardingTable shardingTable : list) {
            if(table.equals(shardingTable.table())) return true;
        }
        return false;
    }
    
    private Class<?> classForName(String beanClassName) {
        try {
            Class<?> cla = Class.forName(beanClassName);
            return cla;
        } catch (ClassNotFoundException e) {
            throw new SwiftRuntimeException(beanClassName + "创建不了类");
        }
    }
}
