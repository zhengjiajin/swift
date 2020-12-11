/*
 * @(#)TestService.java   1.0  2019年2月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.monitor;

import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SynInterface;

/**
 * 用于应用服务监控,此类不会打印什何日志 
 * @author zhengjiajin
 * @version 1.0 2019年2月14日
 */
@Service("test")
public class TestService implements SynInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    public DataModel doService(ServiceRequest req) {
        return null;
    }

}
