/*
 * @(#)Jpa.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 注意高并发场景不能使用save和saveAndFlush,除非加锁
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
public interface Jpa<T, ID extends Serializable> extends JpaRepository<T, ID> {
   @Override
   @Deprecated
   public void deleteAll();
   /**
    * 兼容旧JPA,防止业务代码大量报错
    * @param id
    */
   public <S extends T> List<S> save(Iterable<S> entities);
   /**
    * 通过ID返回某固体实到
    * @param id
    * @return 查环到返回NULL
    */
   public T findOne(ID id);
   /**
    * 兼容旧JPA,防止业务代码大量报错
    * @param id
    */
   public boolean exists(ID id);
   /**
    * 兼容旧JPA,防止业务代码大量报错
    * @param id
    */
   public void delete(ID id);
   /**
    * 兼容旧JPA,防止业务代码大量报错
    * @param id
    */
   public <S extends T> void delete(Iterable<S> entities);
   
}
