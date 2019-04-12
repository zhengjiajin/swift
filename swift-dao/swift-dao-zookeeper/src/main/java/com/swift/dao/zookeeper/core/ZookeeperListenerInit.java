/*
 * @(#)ZookeeperListenerInit.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.zookeeper.core;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import com.swift.core.env.EnvLoader;
import com.swift.dao.zookeeper.Zookeeper.ZookeeperListener;
import com.swift.dao.zookeeper.annontation.ZKListener;
import com.swift.exception.SwiftRuntimeException;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.io.PropertiesUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Component
public class ZookeeperListenerInit extends ApplicationObjectSupport{

    @Autowired
    private ZookeeperClient zookeeperClient;
    
    private Properties properties = PropertiesUtil.getProperties(EnvLoader.getEnv().getEnvPath()+"zookeeper.properties");
    
    private List<TreeCache> allTreeCache = new CopyOnWriteArrayList<TreeCache>();
    
    @PostConstruct
    private void createListener() {
        Map<String,ZookeeperListener> map = super.getApplicationContext().getBeansOfType(ZookeeperListener.class);
        if(map==null || map.isEmpty()) return;
        String namespace = properties.getProperty("zookeeper.znode.root");
        for(ZookeeperListener listener:map.values()) {
            ZKListener zkan = AnnotationUtil.getAnnotation(listener.getClass(), ZKListener.class);
            if(zkan==null) continue;
            //设置节点的cache
            TreeCache treeCache = new TreeCache(zookeeperClient.getClient(), listener(namespace,zkan.value()));
            //设置监听器和处理过程
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    if(!TypeUtil.inList(zkan.type(), event.getType())) {
                        return;
                    }
                    if(event.getData()!=null && !zkan.listenerChild() && !listener(namespace,zkan.value()).equals(event.getData().getPath())) {
                        return;
                    }
                    listener.listener(event);
                }
            });
            //开始监听
            try {
                treeCache.start();
                allTreeCache.add(treeCache);
            } catch (Exception e) {
                treeCache.close();
                throw new SwiftRuntimeException(listener.getClass()+"监听zookeeper失败:"+listener(namespace,zkan.value()));
            }
        }
    }
    
    private String listener(String namespace,String value) {
        if(!value.startsWith("/")) value = "/"+value;
        if(TypeUtil.isNull(namespace) || namespace.equals("/")) return value;
        if(!namespace.startsWith("/")) namespace = "/"+namespace;
        if(namespace.endsWith("/")) namespace = namespace.substring(0,namespace.length()-1);
        if(value.startsWith(namespace+"/")) return value;
        return namespace+value;
    }
    
    @PreDestroy
    private void closeListener() {
        for(TreeCache treeCache : allTreeCache) {
            treeCache.close();
        }
    }
    
}
