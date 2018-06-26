/*
 * @(#)NullMethodFilter.java   1.0  2014-5-17
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.core.filter.RequestFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;


/**
 * 添加说明 
 * @author jiajin
 * @version 1.0 2014-5-17
 */
@Service
public class NullMethodFilter implements RequestFilter {
    private final static Logger log = LoggerFactory.getLogger(NullMethodFilter.class);

    @Override
    public void doFilter(ServiceRequest req) {
        if (req.getMethod() == null) {
            String msg = "您发送请求中没有处理能力method";
            log.error(msg);
            throw new ServiceException(ResultCode.NO_METHOD, msg);
        }
    }

   

}
