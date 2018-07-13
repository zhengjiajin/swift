/*
 * @(#)TestViPojoService.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.vali.service;

import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SimpleInterface;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
@Service("testViPojoService")
public class TestViPojoService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    public DataModel doService(ServiceRequest req) {
        System.out.println(req.getData());
        return req.getData();
    }

}
