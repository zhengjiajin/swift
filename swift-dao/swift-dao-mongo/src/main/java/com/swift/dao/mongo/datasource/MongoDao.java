/*
 * @(#)MongoDao.java   1.0  2020年9月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.mongo.datasource;

import java.util.List;

import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年9月8日
 */
@NoRepositoryBean
public interface MongoDao<T, ID> extends MongoRepository<T, ID>{

    /**
     * 通过ID返回某固体实到
     * @param id
     * @return 查环到返回NULL
     */
    public T findOne(ID id);
    /**
     * 通过ID返回某固体实到
     * @param id
     * @return 查环到返回NULL
     */
    public T getOne(ID id);
    /**
     * 兼容旧JPA,防止业务代码大量报错
     * @param id
     */
    public boolean exists(ID id);
    /**
     * 兼容旧JPA,防止业务代码大量报错
     * @param id
     */
    public <S extends T> void delete(Iterable<S> entities);
    /**
     * 通过语句得到结果
     * @param criteria
     * @return
     */
    public T findOne(Query query);
    /**
     * 通过语句得到结果集
     * @param criteria
     * @return
     */
    public List<T> findList(Query query);
    
    /**
     * 得到结果数
     * @param query
     * @return
     */
    public long count(Query query);
    /**
     * 得到Distinct结果集
     * @param query
     * @param disClass
     * @return
     */
    public <S extends T> List<S> findDistinct(Query query,String field,Class<S> disClass);
    /**
     * 通过坐标查询附近的结果集
     * @param near
     * @return
     */
    public GeoResults<T> geoNear(NearQuery near);
    /**
     * 统计算法
     * @param aggregation
     * @param outputType
     * @return
     */
    public <O> AggregationResults<O> aggregate(Aggregation aggregation,Class<O> outputType);
}
