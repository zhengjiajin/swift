/*
 * @(#)ServiceIndexNameFactory.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es.factory;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swift.util.type.TypeUtil;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * 需要导入的数据表名称
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Service
public class ServiceIndexNameFactory {
    private final static Logger log = LoggerFactory.getLogger(ServiceIndexNameFactory.class);

    @Value("${es.cluster.name:elasticsearch}")
    private String CLUSTER_NAME = "";

    @Value("${es.cluster.ips:}")
    private String CLUSTER_IPS = "";
    /*
     * 分隔符
     */
    private final String CLUSTER_IPS_SPLIT = ",";

    @Value("${es.cluster.port}")
    private int CLUSTER_PORT = 9300;

    private TransportClient client;

    @PostConstruct
    protected void init() {
        try {
            if(TypeUtil.isNull(CLUSTER_IPS)) return;
            Settings settings = Settings.builder().put("cluster.name", CLUSTER_NAME).build();
            this.client = new PreBuiltTransportClient(settings);
            for (String ip : CLUSTER_IPS.split(CLUSTER_IPS_SPLIT)) {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), CLUSTER_PORT));
            }
        } catch (Throwable ex) {
            log.error(CLUSTER_IPS + ";" + CLUSTER_NAME + "出错", ex);
        }
    }

    public TransportClient getClient() {
        return this.client;
    }

}
