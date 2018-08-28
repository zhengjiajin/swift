/*
 * @(#)ZookeeperImpl.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.zookeeper.core;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.dao.zookeeper.Zookeeper;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Component
public class ZookeeperImpl implements Zookeeper {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperImpl.class);
    
    @Autowired
    private ZookeeperClient zookeeperClient;
    
    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#getNodesList(java.lang.String)
     */
    @Override
    public List<String> getNodesList(String path) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return null;
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#getDataNode(java.lang.String)
     */
    @Override
    public byte[] getDataNode(String path) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return null;
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#checkExists(java.lang.String)
     */
    @Override
    public boolean checkExists(String path) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            Stat stat = client.checkExists().forPath(path);
            if(stat==null) return false;
            return true;
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return false;
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#getStat(java.lang.String)
     */
    @Override
    public Stat getStat(String path) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            return client.checkExists().forPath(path);
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return null;
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#createNode(java.lang.String, org.apache.zookeeper.CreateMode)
     */
    @Override
    public boolean createNode(String path,byte[] data,CreateMode mode) {
        CuratorFramework client = zookeeperClient.getClient();
        if(mode==null) mode = CreateMode.PERSISTENT;
        try {
            ACLBackgroundPathAndBytesable<String> bud= client.create().creatingParentsIfNeeded().withMode(mode);
            if(data==null || data.length<=0) {
                bud.forPath(path);
            }else {
                bud.forPath(path, data);
            }
            return true;
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return false;
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#setDataNode(java.lang.String, byte[])
     */
    @Override
    public void setDataNode(String path, byte[] message) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            client.setData().forPath(path, message);
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
        }
    }

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper#deleteDataNode(java.lang.String)
     */
    @Override
    public boolean deleteDataNode(String path) {
        CuratorFramework client = zookeeperClient.getClient();
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e) {
            log.error("zookeeper操作异常",e);
            return false;
        }
       
    }

}
