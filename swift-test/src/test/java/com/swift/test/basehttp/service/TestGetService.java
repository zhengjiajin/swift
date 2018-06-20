/*
 * @(#)TestGetService.java   1.0  2018年6月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.basehttp.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;

import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SimpleInterface;
import com.swift.core.validator.DataValidator;
import com.swift.core.validator.annotation.ParamMin;
import com.swift.core.validator.annotation.ParamNull;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月12日
 */
@Service("testGetServiceV1.0")
public class TestGetService implements SimpleInterface{

    /** 
     * @see com.swift.core.service.SimpleInterface#doService(com.swift.core.model.ServiceRequest)
     */
    @Override
    @DataValidator(Null= 
                    {@ParamNull(param="aaa",anno=@Null(message="aaa不能为空")),
                     @ParamNull(param="bbb.xxx",anno=@Null(message="xxx不能为空"))
                    },
                   Min= {
                       @ParamMin(param="ccc",anno=@Min(10))
                    })
    public DataModel doService(ServiceRequest req) {
        return req.getData();
    }

}
