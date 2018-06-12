/*
 * @(#)TestGetService.java   1.0  2018年6月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.basehttp.serviceV1_1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Service;

import com.swift.core.model.FileDefinition;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.AbstractBeanDataModel;
import com.swift.core.service.SimpleInterface;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月12日
 */
@Service("testGetServiceV1.1")
public class TestGetService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    public FileDefinition doService(ServiceRequest req) {
        GetModel get = AbstractBeanDataModel.mapToBean(req.getData(), GetModel.class);
        System.out.println(get);
        FileDefinition file = new FileDefinition();
        try {
            file.setInputStream(new FileInputStream("G:\\dog1.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

}
