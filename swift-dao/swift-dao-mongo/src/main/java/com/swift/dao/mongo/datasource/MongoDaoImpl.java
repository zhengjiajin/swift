/*
 * @(#)MongoDaoImpl.java   1.0  2020年9月9日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.mongo.datasource;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年9月9日
 */
public class MongoDaoImpl<T, ID> extends SimpleMongoRepository<T,ID> implements MongoDao<T, ID> {

    protected final MongoOperations mongoTemplate;

    protected final MongoEntityInformation<T, ID> entityInformation;
    
    /**
     * @param metadata
     * @param mongoOperations
     */
    public MongoDaoImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoTemplate=mongoOperations;
        this.entityInformation = metadata;
    }

    protected Class<T> getEntityClass(){
        return entityInformation.getJavaType();
    }
    

    /** 
     * @see com.swift.dao.db.jpa.Jpa#findOne(java.io.Serializable)
     */
    @Override
    public T findOne(ID id) {
        Optional<T> optional = super.findById(id);
        if(optional==null) return null;
        if(!optional.isPresent()) return null;
        return optional.get();
    }
    
    @Transactional
    public void deleteById(ID id) {
        try {
            super.deleteById(id);
        }catch(EmptyResultDataAccessException ex) {
            //找不到删除结果不执行异常
            return;
        }
    }

    /** 
     * @see com.swift.dao.db.jpa.Jpa#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(ID id) {
        return super.existsById(id);
    }

    /** 
     * @see com.swift.dao.db.jpa.Jpa#delete(java.lang.Iterable)
     */
    @Override
    @Transactional
    public <S extends T> void delete(Iterable<S> entities) {
        super.deleteAll(entities);
    }
    
    @Override
    public T getOne(ID id) {
        return this.findOne(id);
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#findOne(org.springframework.data.mongodb.core.query.Criteria)
     */
    @Override
    public T findOne(Query query) {
        
      
        
        return mongoTemplate.findOne(query, getEntityClass());
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#findList(org.springframework.data.mongodb.core.query.Criteria)
     */
    @Override
    public List<T> findList(Query query) {
        return mongoTemplate.find(query, getEntityClass());
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#count(org.springframework.data.mongodb.core.query.Query)
     */
    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, getEntityClass());
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#findDistinct(org.springframework.data.mongodb.core.query.Query, java.lang.Class)
     */
    @Override
    public <S extends T> List<S> findDistinct(Query query,String field, Class<S> disClass) {
        return   mongoTemplate.findDistinct(query, field, getEntityClass(), disClass);
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#geoNear(org.springframework.data.mongodb.core.query.NearQuery)
     */
    @SuppressWarnings("deprecation")
    @Override
    public GeoResults<T> geoNear(NearQuery near) {
        
        return mongoTemplate.geoNear(near, getEntityClass());
    }

    /** 
     * @see com.swift.dao.mongo.datasource.MongoDao#aggregate(org.springframework.data.mongodb.core.aggregation.Aggregation, java.lang.Class)
     */
    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<O> outputType) {
        return mongoTemplate.aggregate(aggregation, getEntityClass(), outputType);
    }
}
