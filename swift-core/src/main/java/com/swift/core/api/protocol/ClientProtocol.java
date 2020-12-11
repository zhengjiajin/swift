/*
 * @(#)ClientProtocol.java   1.0  2020年12月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api.protocol;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;

/**
 * 把业务对象转换为HTTPCLIENT发送类对象,HTTPCLIENT使用
 * @author zhengjiajin
 * @version 1.0 2020年12月1日
 */
public interface ClientProtocol<T, R> {
     /*
      * 把业务请求对象转换为发送类对象
      */
     public T toRequest(ServiceRequest req);
     /*
      * 把返回类对象转换为业务反回对象
      */
     public ServiceResponse toResponse(ServiceRequest req,R r);
}
