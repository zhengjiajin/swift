/*
 * @(#)MongodbConfig.java   1.0  2020年9月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.swift.dao.mongo.datasource.MongoDaoImpl;
import com.swift.exception.extend.SystemException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年9月8日
 */
@Configuration
@EnableMongoRepositories(value="com.swift",repositoryBaseClass = MongoDaoImpl.class)
public class MongodbConfig {

    @Value("${mongoUri}")
    private String mongoUri;

    @Value("${mongoDatabase}")
    private String mongoDatabase;

    @Bean
    public MongoClient  mongoClient() {
        if (TypeUtil.isNull(mongoUri)) return null;
        if (TypeUtil.isNull(mongoDatabase)) return null;
        if (mongoUri.indexOf("maxPoolSize") == -1) {
            mongoUri=mongoUri+"&maxPoolSize=30";
        }
        if (mongoUri.indexOf("minPoolSize") == -1) {
            mongoUri=mongoUri+"&minPoolSize=5";
        }
        if (mongoUri.indexOf("maxIdleTimeMS") == -1) {
            mongoUri=mongoUri+"&maxIdleTimeMS=30000";
        }
        if (mongoUri.indexOf("waitQueueMultiple") == -1) {
            mongoUri=mongoUri+"&waitQueueMultiple=30";
        }
        if (mongoUri.indexOf("waitQueueTimeoutMS") == -1) {
            mongoUri=mongoUri+"&waitQueueTimeoutMS=5000";
        }
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabase database(MongoClient mongoClient) {
        if(mongoClient==null) return null;
        MongoDatabase database = mongoClient.getDatabase(mongoDatabase);
        if (database == null) throw new SystemException(mongoDatabase + "库末创建");
        return database;
    }
    
    /**
     * 注册mongodb操作类
     * @param mongoClient
     * @return
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        if(mongoClient==null) return null;
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, mongoDatabase);
        return mongoTemplate;
    }
    
}
