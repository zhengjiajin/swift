/*
 * @(#)Jpa.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.jpa;

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
public interface Jpa<T, ID extends Serializable> extends QueryByExampleExecutor<T>,PagingAndSortingRepository<T, ID> {
   @Override
   @Deprecated
   void deleteAll();
}
