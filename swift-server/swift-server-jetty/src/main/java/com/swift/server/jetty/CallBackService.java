/*
 * @(#)CallBackService.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty;

import com.swift.core.model.ServiceResponse;

/**
 * 添加说明 
 * @author jiajin
 * @version 1.0 2014-5-16
 */
public interface CallBackService {
    public void callback(ServiceResponse res) ;
}
