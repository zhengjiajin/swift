/*
 * @(#)Filter.java   1.0  2014-5-17
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.filter;

import com.swift.core.model.ServiceRequest;


/**
 * 收到消息的过滤器 
 * @author jiajin
 * @version 1.0 2014-5-17
 */
public interface RequestFilter {
    public void doFilter(ServiceRequest req);
}
