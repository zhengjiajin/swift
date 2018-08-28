/*
 * @(#)TestZookeeper.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.zookeeper.Zookeeper;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
public class TestZookeeper extends BaseJunit4Test{

    @Autowired
    private Zookeeper zookeeper;
    
    
    @Test
    public void testInit() {
       List<String> list = zookeeper.getNodesList("/");
       System.out.println(list);
       System.out.println(zookeeper.checkExists("/testlist"));
       System.out.println(zookeeper.createNode("/testlist", "test".getBytes(), null));
       System.out.println(zookeeper.checkExists("/testlist"));
       System.out.println(zookeeper.getNodesList("/"));
       System.out.println(zookeeper.getDataNode("/testlist"));
       zookeeper.setDataNode("/testlist", "bbb".getBytes());
       System.out.println(zookeeper.createNode("/testlist/zzz", "test".getBytes(), null));
       System.out.println(zookeeper.deleteDataNode("/testlist"));
    }
}
