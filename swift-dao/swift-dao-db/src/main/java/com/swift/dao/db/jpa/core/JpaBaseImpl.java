/*
 * @(#)JpaBaseImpl.java   1.0  2020年4月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.jpa.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.swift.dao.db.jpa.Jpa;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年4月14日
 */
public class JpaBaseImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements Jpa<T, ID> {

    protected JpaEntityInformation<T, ?> entityInformation;
    
    protected EntityManager entityManager;
  
    public JpaBaseImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation=entityInformation;
        this.entityManager=entityManager;
    }

    public JpaBaseImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
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
     * @see com.swift.dao.db.jpa.Jpa#delete(java.io.Serializable)
     */
    @Override
    @Transactional
    public void delete(ID id) {
        this.deleteById(id);
    }
    /**
     * 改成提交事务的保存方式
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(S)
     */
    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        S result = super.save(entity);
        flush();
        return result;
    }

    /** 
     * @see com.swift.dao.db.jpa.Jpa#save(java.lang.Iterable)
     */
    @Override
    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities) {
        return this.saveAll(entities);
    }
    
    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities){
        if(entities==null) return null;
        List<S> result = new ArrayList<S>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
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
    
}
