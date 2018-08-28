/*
 * @(#)ZookeeperClient.java   1.0  2018年8月24日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.zookeeper;

import java.util.List;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月24日
 */
public interface Zookeeper {

    public List<String> getNodesList(String path);
    
    public byte[] getDataNode(String path);

    public boolean checkExists(String path);
    
    public Stat getStat(String path);
    
    public boolean createNode(String path,byte[] data,CreateMode mode);
    
    public void setDataNode(String path,byte[] message);
    
    public boolean deleteDataNode(String path);
    
    public interface ZookeeperListener {
        
        public void listener(TreeCacheEvent event);
        
    }
}
