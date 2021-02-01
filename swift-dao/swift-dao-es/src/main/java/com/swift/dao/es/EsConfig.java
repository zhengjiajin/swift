/*
 * @(#)EsConfig.java   1.0  2021年1月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.swift.dao.es.factory.ServiceIndexNameFactory;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年1月29日
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.swift")
public class EsConfig extends AbstractElasticsearchConfiguration{

    @Autowired
    private ServiceIndexNameFactory serviceIndexNameFactory;
    
    
    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchOperations elasticsearchOperations() {
        if(!serviceIndexNameFactory.isConnect()) return null;
        return new ElasticsearchRestTemplate(elasticsearchClient(), elasticsearchConverter(), resultsMapper());
    }
    /** 
     * @see org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration#elasticsearchClient()
     */
    @Override
    public RestHighLevelClient elasticsearchClient() {
        if(!serviceIndexNameFactory.isConnect()) return null;
        return serviceIndexNameFactory.getClient();
    }
}
