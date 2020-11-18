/*
 * @(#)ZookeeperConfig.java   1.0  2018年8月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.zk.core;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.swift.exception.extend.SystemException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月27日
 */
@Component
public class ZookeeperClient {

    private CuratorFramework client;
    @Value("${zookeeper.servers}")
    private String servers;
    
    @Value("${zookeeper.userName}")
    private String userName;
    
    @Value("${zookeeper.servers}")
    private String password;
    
    @Value("${zookeeper.connection.timeout}")
    private Integer connectionTimeout;
    
    @Value("${zookeeper.session.timeout}")
    private Integer sessionTimeout;
    
    @Value("${zookeeper.znode.root}")
    private String namespace;
    
    @Value("${zookeeper.recovery.interval}")
    private Integer interval;
    
    @Value("${zookeeper.recovery.retries}")
    private Integer retries;
    
    @PostConstruct
    private void init() {
        if(TypeUtil.isNull(servers)) return;
        Builder builder = CuratorFrameworkFactory.builder();
        if(TypeUtil.isNotNull(userName) && TypeUtil.isNotNull(password)) {
            byte[] auth = (userName+":"+password).getBytes();
            builder.aclProvider( createACLProvider(userName, password)).authorization("digest", auth);
        }
        builder.connectionTimeoutMs(TypeUtil.toInt(connectionTimeout, 5000));
        
        builder.connectString(servers);
        
        builder.sessionTimeoutMs(TypeUtil.toInt(sessionTimeout, 18000));
        
        if(TypeUtil.isNull(namespace)) throw new SystemException("zookeeper.properties里的zookeeper.znode.root必须配置");
        builder.namespace(namespace);
        
        int recoveryInterval = TypeUtil.toInt(interval, 1000);
        int recoveryRetries = TypeUtil.toInt(retries, Integer.MAX_VALUE);
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
        if(client==null) throw new SystemException("没有配置zookeeper");
        if(!CuratorFrameworkState.STARTED.equals(client.getState())) throw new SystemException("没有配置zookeeper未连接成功");
        return client;
    }
    
}
