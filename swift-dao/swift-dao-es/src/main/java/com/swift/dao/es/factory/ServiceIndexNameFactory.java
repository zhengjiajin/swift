/*
 * @(#)ServiceIndexNameFactory.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es.factory;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swift.exception.extend.SystemException;
import com.swift.util.type.TypeUtil;

/**
 * 需要导入的数据表名称
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Service
public class ServiceIndexNameFactory {
    private final static Logger log = LoggerFactory.getLogger(ServiceIndexNameFactory.class);

    @Value("${es.hostname}")
    private String HOSTNAME;

    @Value("${es.port:9200}")
    private Integer PORT = 9200;

    @Value("${es.scheme:http}")
    private String SCHEME = "http";

    @Value("${es.username}")
    private String USERNAME;

    @Value("${es.password}")
    private String PASSWORD;

    private RestHighLevelClient highClient;
    
    public static final RequestOptions COMMON_OPTIONS;
    
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // 默认缓冲限制为100MB
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(100 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @PostConstruct
    protected void init() {
        try {
            if (TypeUtil.isNull(HOSTNAME)) return;
            if (TypeUtil.isNull(USERNAME)) return;
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));

            RestClientBuilder builder = RestClient.builder(new HttpHost(HOSTNAME, PORT))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
            highClient = new RestHighLevelClient(builder);
        } catch (Throwable ex) {
            log.error("es连接异常" + HOSTNAME + ";" + USERNAME + "出错", ex);
        }
    }

    public RestHighLevelClient getClient() {
        if (this.highClient == null) throw new SystemException("未建产ES连接，不能操作");
        return this.highClient;
    }
    
    public void restart() {
        destroy();
        init();
    }

    @PreDestroy
    protected void destroy() {
        try {
            this.highClient.close();
        } catch (IOException e) {
        }
    }

}
