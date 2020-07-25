/*
 * @(#)ServiceFactory.java   1.0   2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.swift.core.spring.proxy.ProxyMapper;
import com.swift.exception.extend.SystemException;
import com.swift.util.bean.AnnotationUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0  2020年4月15日
 */
public class ProxyServiceFactory<T> implements FactoryBean<T> {
    private Class<T> interfaceType;
    
    private ProxyMapper proxyMapper;
    
    public ProxyServiceFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
        if(this.interfaceType==null) throw new SystemException("interfaceType不能为空");
        this.proxyMapper = AnnotationUtil.getAnnotation(interfaceType, ProxyMapper.class);
        if(this.proxyMapper==null) throw new SystemException("需要代理的接口必须包含ProxyMapper注解");
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        //这里主要是创建接口对应的实例，便于注入到spring容器中
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] {interfaceType}, newInvocationObject());
    }
    
    private InvocationHandler newInvocationObject() {
        try {
            InvocationHandler mapperObj = (InvocationHandler) proxyMapper.value().newInstance();
            return mapperObj;
        } catch (SystemException ex) {
            throw ex;
        } catch (Exception e) {
            throw new SystemException(interfaceType + "创建不了类", e);
        }
    }
    
    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }
 
    @Override
    public boolean isSingleton() {
        return true;
    }
}
