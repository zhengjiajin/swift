/*
 * @(#)TestService.java   1.0  2019年2月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.monitor.service;

import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SimpleInterface;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年2月14日
 */
@Service("test")
public class TestService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    public DataModel doService(ServiceRequest req) {
        return null;
    }

}
