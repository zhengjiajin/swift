/*
 * @(#)HttpDomainUtil.java   1.0  2020年12月9日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.util;

import java.net.URI;
import java.net.URISyntaxException;

import com.swift.core.model.ServiceRequest;
import com.swift.core.spring.Spring;
import com.swift.exception.extend.SystemException;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;
import com.swift.util.type.UrlUtil;

/**
 * 处理地址连接工具类
 * @author zhengjiajin
 * @version 1.0 2020年12月9日
 */
public class HttpDomainUtil {

    public static String toStringDamain(ServiceRequest req) {
        String domain = req.getDomain();
        String sysId = req.getSysId();
        if(TypeUtil.isNull(domain)) {
            domain=Spring.getProperties().getProperty("domain");
        }
        
        if(TypeUtil.isNotNull(sysId) && !domain.startsWith(sysId) && !UrlUtil.isUrl(domain)) {
            domain=sysId+"."+domain;
        }
        
        if(!UrlUtil.isUrl(domain)) {
            domain="http://"+domain;
        }
        
        if(!req.getInterfaceVersion().equals(ServiceRequest.DEFAULT_VERSION)) {
            if(domain.indexOf("/V"+req.getInterfaceVersion())==-1) {
                domain=domain+"/V"+req.getInterfaceVersion();
            }
        }
        if(TypeUtil.isNotNull(req.getMethod()) && !domain.endsWith(req.getMethod())) {
            domain=domain+"/"+req.getMethod();
        }
        
        return domain;
    }
    
    public static URI toUriDamain(ServiceRequest req) {
        try {
            return new URI(toStringDamain(req));
        } catch (URISyntaxException e) {
            throw new SystemException("创建地址错误:"+JsonUtil.toJson(req));
        }
    }
}
