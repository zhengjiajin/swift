/*
 * @(#)TestListerner.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.impl;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.stereotype.Service;

import com.swift.dao.zookeeper.Zookeeper.ZookeeperListener;
import com.swift.dao.zookeeper.annontation.ZKListener;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Service
@ZKListener(value="/testlist",listenerChild=false)
public class TestListerner implements ZookeeperListener {

    /** 
     * @see com.swift.dao.zookeeper.Zookeeper.ZookeeperListener#listener(org.apache.curator.framework.recipes.cache.TreeCacheEvent, org.apache.curator.framework.recipes.cache.ChildData, java.util.Map)
     */
    @Override
    public void listener(TreeCacheEvent event) {
        System.out.println(event.getType()+"新路径:"+event.getData());
    }

}
