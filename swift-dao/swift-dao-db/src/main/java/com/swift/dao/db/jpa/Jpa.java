/*
 * @(#)Jpa.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 注意高并发场景不能使用save和saveAndFlush,除非加锁
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
public interface Jpa<T, ID extends Serializable> extends JpaRepository<T, ID> {
   @Override
   @Deprecated
   void deleteAll();
   
   @Override
   @Deprecated
   T getOne(ID id);
}
