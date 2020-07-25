/*
 * @(#)CallBackService.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.service.processor;

import com.swift.core.model.ServiceResponse;

/**
 * 异步请求返回响应接口 
 * @author jiajin
 * @version 1.0 2014-5-16
 */
public interface CallBackService {
    public void callback(ServiceResponse res) ;
}
