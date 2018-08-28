/*
 * @(#)ZookeeperConfig.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.zookeeper.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.springframework.stereotype.Component;

import com.swift.exception.SwiftRuntimeException;
import com.swift.util.io.PropertiesUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Component
public class ZookeeperClient {

    private CuratorFramework client;
    
    @PostConstruct
    private void init() {
        Properties properties = PropertiesUtil.getProperties("zookeeper.properties");
        if(properties==null) return;
        String servers = properties.getProperty("zookeeper.servers");
        if(TypeUtil.isNull(servers))  throw new SwiftRuntimeException("zookeeper.properties里的zookeeper.servers必须配置");
        Builder builder = CuratorFrameworkFactory.builder();
        String userName=properties.getProperty("zookeeper.userName");
        String password=properties.getProperty("zookeeper.password");
        if(TypeUtil.isNotNull(userName) && TypeUtil.isNotNull(password)) {
            byte[] auth = (userName+":"+password).getBytes();
            builder.aclProvider( createACLProvider(userName, password)).authorization("digest", auth);
        }
        int connectionTimeoutMs=TypeUtil.toInt(properties.getProperty("zookeeper.connection.timeout"), 5000);
        builder.connectionTimeoutMs(connectionTimeoutMs);
        
        builder.connectString(servers);
        
        int sessionTimeoutMs=TypeUtil.toInt(properties.getProperty("zookeeper.session.timeout"), 18000);
        builder.sessionTimeoutMs(sessionTimeoutMs);
        
        String namespace = properties.getProperty("zookeeper.znode.root");
        if(TypeUtil.isNull(namespace))   throw new SwiftRuntimeException("zookeeper.properties里的zookeeper.znode.root必须配置");
        builder.namespace(namespace);
        
        int recoveryInterval = TypeUtil.toInt(properties.getProperty("zookeeper.recovery.interval"), 1000);
        int recoveryRetries = TypeUtil.toInt(properties.getProperty("zookeeper.recovery.retries"), Integer.MAX_VALUE);
        client=builder.retryPolicy(new RetryNTimes(recoveryRetries, recoveryInterval)).build();
        client.start();
    }


    private ACLProvider createACLProvider(String userName, String password) {
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl ;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl ==null){
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    acl.add(new ACL(Perms.ALL, new Id("auth", userName+":"+password) ));
                    this.acl = acl;
                }
                return acl;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        return aclProvider;
    }
    
    public CuratorFramework getClient() {
        if(client==null) throw new SwiftRuntimeException("没有配置zookeeper.properties");
        if(!CuratorFrameworkState.STARTED.equals(client.getState())) throw new SwiftRuntimeException("没有配置zookeeper未连接成功");
        return client;
    }
    
}
