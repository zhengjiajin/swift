/*
 * @(#)ResFilter.java   1.0  2015-2-13
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.filter;

import com.swift.core.model.ServiceResponse;

/**
 * 发送消息的过滤器 
 * @author jiajin
 * @version 1.0 2015-2-13
 */
public interface ResponseFilter {
    public void doFilter(ServiceResponse res);
}
