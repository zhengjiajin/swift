/*
 * @(#)BeanUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.bean;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.core.ResolvableType;

import com.swift.exception.extend.SystemException;


/**
 * 类属性复制转换处理类库
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class BeanUtil {
    
    public static void copyProperties(Object dest, Object orig) {
        try {
            if(orig==null) return;
            if(dest==null) return;
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);  
            BeanUtils.copyProperties(dest, orig);
        } catch (Exception ex) {
            throw new SystemException("bean copy exception",ex);
        }
    }
    /*
     * 不执行静态块
     */
    public static Class<?> classForName(String className) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new SystemException("ClassNotFoundException",e);
        }
    }
    
    public static <T> T newInstance(Class<T> cla) {
        try {
            return cla.newInstance();
        } catch (Exception e) {
            throw new SystemException("类创建异常",e);
        } 
    }
    /**
     * 得到类的泛型类
     * @param cla
     * @return
     */
    public static ResolvableType[] getTonClass(Class<?> cla){
        ResolvableType resolvableType = ResolvableType.forClass(cla).getSuperType();
        ResolvableType[] types = resolvableType.getSuperType().getGenerics();
        return types;
    }
}
