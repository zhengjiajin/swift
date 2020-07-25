/*
 * @(#)IBaseModel.java   1.0  2015年10月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 数据表基类，公用处理方法
 * 
 * @author zhengjiajin
 * @version 1.0 2015年10月19日
 */
public abstract class IBaseModel extends AbstractBeanDataModel {
	private static final Logger log = LoggerFactory.getLogger(IBaseModel.class);

	@Override
	public String toString() {
		try {
			return JsonUtil.toJson(this);
		} catch (Exception ex) {
			log.error("JSON转换错误:", ex);
		}
		return null;
	}

	@JsonIgnore
	public boolean isNull() {
	    return TypeUtil.isNull(this.keySet());
	}
}
