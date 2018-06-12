/*
 * @(#)SimpleInterface.java   1.0  2015年8月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;

/**
 * 主服务接口
 * 实现此接口可使用本包内的所有标签
 * 接口版本控制使用Spring的BEAN名称，BEAN名称统一命名为如：???V1.0
 * T为返回对象，可以任何可序列化的对象
 * @author zhengjiajin
 * @version 1.0 2015年8月5日
 */
public interface SimpleInterface {
    public DataModel doService(ServiceRequest req);
}
