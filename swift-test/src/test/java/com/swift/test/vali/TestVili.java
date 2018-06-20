/*
 * @(#)TestVili.java   1.0  2018年6月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.vali;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.model.ServiceRequest;
import com.swift.core.service.SimpleInterface;
import com.swift.test.BaseJunit4Test;
import com.swift.util.date.DateUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月15日
 */
public class TestVili extends BaseJunit4Test{
    
    @Autowired
    private SimpleInterface testViModelService;

    @Autowired
    private SimpleInterface testViPojoService;
    
    @Test
    public void testModel() {
        ServiceRequest req = new ServiceRequest();
        req.getData().addObject("keyId", "sdfs");
        req.getData().addObject("valueContent", "zzzz");
        req.getData().addObject("testAbc", "zzzz");
        req.getData().addObject("assertTrue", true);
        req.getData().addObject("assertFalse", false);
        
        req.getData().addObject("Min", "asdfasdddfs");
        req.getData().addObject("Max", "arrrw");
        req.getData().addObject("DecimalMin", 11.5);
        req.getData().addObject("Email", "dsfasdf@qq.com");
        req.getData().addObject("Length", "dsfa");
        req.getData().addObject("Past", DateUtil.toDate("2018-06-19 15:31:30"));
        req.getData().addObject("Future", DateUtil.toDate("2018-07-19 15:31:30"));
        System.out.println(testViModelService.doService(req));
    }
    
    @Test
    public void testPojo() {
        ServiceRequest req = new ServiceRequest();
        req.getData().addObject("keyId", "sdfs");
        req.getData().addObject("valueContent", "zzzz");
        System.out.println(testViPojoService.doService(req));
    }
    
}
