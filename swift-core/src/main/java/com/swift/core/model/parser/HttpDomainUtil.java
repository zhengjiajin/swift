/*
 * @(#)HttpDomainUtil.java   1.0  2020年12月9日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.parser;

import java.net.URI;
import java.net.URISyntaxException;

import com.swift.core.model.ServiceRequest;
import com.swift.core.spring.Spring;
import com.swift.exception.ServiceException;
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
        //String sysId = req.getSysId();
        if(TypeUtil.isNull(domain)) throw new ServiceException("请输入你想访问的对端:domain");
        if(domain.indexOf(".")==-1) {
            domain=domain+"."+Spring.getProperties().getProperty("domain");
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
    
    public static String removeHttp(String url) {
        if (TypeUtil.isNull(url)) return url;
        if (url.toLowerCase().startsWith("http://")) {
            return url.substring("http://".length());
        }
        if (url.toLowerCase().startsWith("https://")) {
            return url.substring("https://".length());
        }
        return url;
    }
    
    //得到系统ID
    public static String toSysId(String url) {
        url = removeHttp(url);
        if (TypeUtil.isNull(url)) return null;
        if(url.indexOf(".")==-1 && url.indexOf("/")==-1) return url;
        if(url.indexOf(".")==-1) return null;
        //只有一个.
        if(url.indexOf(".")==url.lastIndexOf("."))  return null;
        return url.substring(0, url.indexOf("."));
    }
    //得到域名
    public static String toDomain(String url) {
        url = removeHttp(url);
        if (TypeUtil.isNull(url)) return null;
        if(url.indexOf(".")==-1) return null;
        //去除后部/xxx
        if(url.indexOf("/")!=-1) {
            url=url.substring(0,url.indexOf("/"));
        }
        //只有一个.
        if(url.indexOf(".")==url.lastIndexOf("."))  return url;
        return url.substring(url.indexOf(".")+1);
    }
    //得到版本
    public static String toInterfaceVersion(String url) {
        url = removeHttp(url);
        if (TypeUtil.isNull(url)) return null;
        if(url.indexOf("/V")==-1) return null;
        url=url.substring(url.indexOf("/V"));
        if(url.indexOf("/")!=url.lastIndexOf("/")) {
            url=url.substring(url.indexOf("/V"),url.lastIndexOf("/"));
        }
        String interfaceVersion = url.replace("/V", "");
        if(interfaceVersion.indexOf("/")!=-1) {
            interfaceVersion = interfaceVersion.substring(0,interfaceVersion.indexOf("/"));
        }
        if(TypeUtil.isNumber(interfaceVersion)) return interfaceVersion;
        return null;
    }
    //得到方法
    public static String toMethod(String url) {
        url = removeHttp(url);
        if (TypeUtil.isNull(url)) return null;
        if(url.indexOf("/")==-1 && url.indexOf(".")==-1) return url;
        if(url.indexOf("/")==-1 && url.indexOf(".")!=-1) return null;
        return url.substring(url.lastIndexOf("/")+1);
    }
    
    
    public static URI toUriDamain(ServiceRequest req) {
        try {
            return new URI(toStringDamain(req));
        } catch (URISyntaxException e) {
            throw new SystemException("创建地址错误:"+JsonUtil.toJson(req));
        }
    }
    
}
