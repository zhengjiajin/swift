/*
 * @(#)DataSourceFactory.java   1.0  2018年5月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.datasource;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.swift.core.filter.EndFilter;
import com.swift.exception.UnknownException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年5月4日
 */
@Component("dataSource")
public class DataSourceFactory extends AbstractRoutingDataSource implements EndFilter{

    private Map<String, List<ComboPooledDataSource>> targetListDataSource = new ConcurrentHashMap<String, List<ComboPooledDataSource>>();

    private Map<Object, Object> targetDataSources = new ConcurrentHashMap<Object, Object>();
    //用于保持同一线程同一连接源
    private static ThreadLocal<Map<String,Integer>> thredLocalDb = new ThreadLocal<Map<String,Integer>>();

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${classpath*:jdbc.properties}")
    private Resource resource;

    private static final String DEFAULT_DB = DataSource.master.getDbName();

    public DataSourceFactory() {
        super.setTargetDataSources(targetDataSources);
    }

    @PostConstruct
    protected void init() throws Exception {
        if (resource == null) return;
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
    }

    private void splitDataSource(String source, String urls, String userNames, String passwords) {
        String[] urlSpilt = urls.split(",");
        String[] userNameSpilt = userNames.split(",");
        String[] passwordSpilt = passwords.split(",");
        for (int i = 0; i < urlSpilt.length; i++) {
            ComboPooledDataSource sourceBean = createComboPooledDataSource(urlSpilt[i], userNameSpilt[i],
                passwordSpilt[i]);
            if (targetListDataSource.get(source) == null)
                targetListDataSource.put(source, new ArrayList<ComboPooledDataSource>());
            targetListDataSource.get(source).add(sourceBean);
            targetDataSources.put(source + i, sourceBean);
        }
    }

    @PreDestroy
    protected void destory() {
        if (targetListDataSource != null) {
            for (List<ComboPooledDataSource> listSource : targetListDataSource.values()) {
                if (TypeUtil.isNull(listSource)) continue;
                for (ComboPooledDataSource source : listSource) {
                    source.close();
                }
            }
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
        if (targetListDataSource == null || targetListDataSource.isEmpty()) throw new UnknownException("数据库未配置");
        List<ComboPooledDataSource> listSource = targetListDataSource.get(source);
        if (listSource == null || listSource.isEmpty()) throw new UnknownException("数据库未配置");
        int i=0;
        if(thredLocalDb.get()==null) {
            thredLocalDb.set(new HashMap<String,Integer>());
        }
        if(thredLocalDb.get().get(source)==null) {
            i = RandomUtils.nextInt(listSource.size());
            thredLocalDb.get().put(source, i);
        }else {
            i=thredLocalDb.get().get(source);
        }
        return source.toString() + i;
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
        bean.setMinPoolSize(5);
        bean.setMaxPoolSize(100);
        bean.setMaxIdleTime(1800);
        bean.setAcquireIncrement(2);
        bean.setMaxStatements(0);
        bean.setInitialPoolSize(5);
        bean.setIdleConnectionTestPeriod(300);
        bean.setAcquireRetryAttempts(30);
        bean.setAcquireRetryDelay(10000);
        bean.setBreakAfterAcquireFailure(false);
        bean.setTestConnectionOnCheckout(false);
        bean.setAutoCommitOnClose(true);
        return bean;
    }

    /** 
     * @see com.swift.core.filter.EndFilter#end()
     */
    @Override
    public void end() {
        thredLocalDb.remove();
    }
}
