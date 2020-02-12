/*
 * @(#)DataSourceFactory.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

import java.beans.PropertyVetoException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.swift.dao.db.datasource.sharding.ShardingConfig;
import com.swift.exception.UnknownException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年5月4日
 */
@Component("dataSource")
public class DataSourceFactory extends AbstractRoutingDataSource {

    private final static Logger log = LoggerFactory.getLogger(DataSourceFactory.class);

    private Map<Object, Object> targetDataSources = new ConcurrentHashMap<Object, Object>();

    @Value("${jdbc.driverClassName:org.gjt.mm.mysql.Driver}")
    private String driverClassName;
    
    @Value("${jdbc.minPoolSize:5}")
    private String minPoolSize;
    
    @Value("${jdbc.maxPoolSize:100}")
    private String maxPoolSize;
    
    @Value("${classpath*:jdbc.properties}")
    private Resource resource;

    private static final String DEFAULT_DB = "master";

    private boolean isStart = false;
    
    @Autowired
    private ShardingConfig shardingConfig;
    
    public DataSourceFactory() {
        super.setTargetDataSources(targetDataSources);
    }

    @PostConstruct
    protected void init() throws Exception {
        if (resource == null || !resource.exists()) return;
        if (TypeUtil.isNull(driverClassName)) throw new RuntimeException("jdbc.driverClassName is null");
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        for (Object keyObj : properties.keySet()) {
            String key = TypeUtil.toString(keyObj);
            if(TypeUtil.isNull(key) || key.indexOf("jdbc.")==-1 || key.indexOf(".url")==-1) {
                continue;
            }
            String source = key.substring(key.indexOf("jdbc.")+5, key.indexOf(".url"));
            String usls = properties.getProperty("jdbc." + source + ".url");
            String userNames = properties.getProperty("jdbc." + source + ".username");
            String passwords = properties.getProperty("jdbc." + source + ".password");
            if (TypeUtil.isNull(usls) || TypeUtil.isNull(userNames) || TypeUtil.isNull(passwords)) return;
            splitDataSource(source, usls, userNames, passwords);
        }
        isStart=true;
    }

    private void splitDataSource(String source, String urls, String userNames, String passwords) {
        String[] urlSpilt = urls.split(",");
        String[] userNameSpilt = userNames.split(",");
        String[] passwordSpilt = passwords.split(",");
        Map<String,DataSource> dataSourceMap = new LinkedHashMap<>();
        for (int i = 0; i < urlSpilt.length; i++) {
            ComboPooledDataSource sourceBean = createComboPooledDataSource(urlSpilt[i], userNameSpilt[i],passwordSpilt[i]);
            dataSourceMap.put(source + i, sourceBean);
        }
        if(TypeUtil.isNull(dataSourceMap)) return;
        if(dataSourceMap.size()>1 || shardingConfig.isShardingDb(source)) {
            targetDataSources.put(source, shardingConfig.getShardingDataSource(source,dataSourceMap));
        }else {
            targetDataSources.put(source, dataSourceMap.values().iterator().next());
        }
        super.afterPropertiesSet();
    }

    
    /**
     * 添加数据源
     * @param dbName
     * @param urls 多数据源用,分隔
     * @param userNames 多数据源用,分隔
     * @param passwords 多数据源用,分隔
     */
    public void addDataSource(String dbName, String urls, String userNames, String passwords) {
        splitDataSource(dbName, urls, userNames, passwords);
    }
    /**
     * 删除数据源
     * @param dbName
     */
    public void delDataSource(String dbName) {
        Object dataSource = targetDataSources.remove(dbName);
        if(dataSource==null) return;
        try {
            if(dataSource instanceof ShardingDataSource) {
                ((ShardingDataSource)dataSource).close();
            }
            if(dataSource instanceof ComboPooledDataSource) {
                ((ComboPooledDataSource)dataSource).close();
            }
        } catch (Exception e) {
            log.error("DataSource Close ERROR",e); 
        }
    }

    @PreDestroy
    protected void destory() {
        if (targetDataSources != null) {
            for (Object dataSource : targetDataSources.values()) {
                if (TypeUtil.isNull(dataSource)) continue;
                try {
                    if(dataSource instanceof ShardingDataSource) {
                        ((ShardingDataSource)dataSource).close();
                    }
                    if(dataSource instanceof ComboPooledDataSource) {
                        ((ComboPooledDataSource)dataSource).close();
                    }
                } catch (Exception e) {
                    log.error("DataSource Close ERROR",e); 
                }
            }
            targetDataSources.clear();
        }
    }

    /**
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
        // applicationContext.getParentBeanFactory();
        String source = HandlerDataSource.getDataSource();
        if (source == null) source= DEFAULT_DB;
        if (targetDataSources == null || targetDataSources.isEmpty()) throw new UnknownException("数据库未配置");
        DataSource dataSource = (DataSource)targetDataSources.get(source);
        if (dataSource == null) throw new UnknownException("数据库未配置");
        return source.toString();
    }

    protected ComboPooledDataSource createComboPooledDataSource(String url, String username, String password) {
        return parentDataSource(dataSourceProperties(username, password), url);
    }

    protected PropertiesEncryptFactoryBean dataSourceProperties(String username, String password) {
        PropertiesEncryptFactoryBean bean = new PropertiesEncryptFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        bean.setProperties(properties);
        return bean;
    }

    protected ComboPooledDataSource parentDataSource(PropertiesEncryptFactoryBean dataSourceProperties, String url) {
        ComboPooledDataSource bean = new ComboPooledDataSource();
        bean.setProperties(dataSourceProperties.getProperties());
        try {
            bean.setDriverClass(driverClassName);
        } catch (PropertyVetoException e) {
            throw new RuntimeException("driverClassName出错");
        }
        bean.setJdbcUrl(url);
        bean.setMinPoolSize(TypeUtil.toInt(minPoolSize));
        bean.setMaxPoolSize(TypeUtil.toInt(maxPoolSize));
        bean.setMaxIdleTime(1800);
        bean.setAcquireIncrement(2);
        bean.setMaxStatements(0);
        bean.setInitialPoolSize(TypeUtil.toInt(minPoolSize));
        bean.setIdleConnectionTestPeriod(300);
        bean.setAcquireRetryAttempts(30);
        bean.setAcquireRetryDelay(10000);
        bean.setBreakAfterAcquireFailure(false);
        bean.setTestConnectionOnCheckout(false);
        bean.setAutoCommitOnClose(true);
        return bean;
    }

    public boolean isStart() {
        return isStart;
    }
}
