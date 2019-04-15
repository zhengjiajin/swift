/*
 * @(#)AsynInterface.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service;

import com.swift.core.model.ServiceRequest;

/**
 * 主服务接口,异步返回接口
 * 实现此接口可使用本包内的所有标签
 * 接口版本控制使用Spring的BEAN名称，BEAN名称统一命名为如：???V1.0 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface AsynInterface extends BaseInterface {

    public void doService(ServiceRequest req);
    
}
