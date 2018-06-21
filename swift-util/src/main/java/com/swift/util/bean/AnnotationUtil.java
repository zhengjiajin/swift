/*
 * @(#)AnnotationUtil.java   1.0  2018年6月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 实现取annotation优先级取注解
 * 实例方法-> 接口方法->实例类->接口类
 * @author zhengjiajin
 * @version 1.0 2018年6月21日
 */
public class AnnotationUtil {
   
    public static <T extends Annotation > T getAnnotation(Class<?> objClass, Method method, Class<T> annotationClass) {
        T t = null;
        if(objClass==null && method!=null) objClass=method.getDeclaringClass();
        if(method!=null) {
            t = method.getAnnotation(annotationClass);
            if (t == null) {
                t = getAutoDbByInterfaceMethod(method,annotationClass);
            }
        }
       
        if (t == null) {
            t = objClass.getAnnotation(annotationClass);
        }
        if (t == null) {
            t = getAutoDbByInterface(objClass,annotationClass);
        }
        return t;
    }
    
    public static <T extends Annotation > T getAnnotation(Class<?> objClass, Class<T> annotationClass) {
        return getAnnotation(objClass, null, annotationClass);
    }
    
    public static <T extends Annotation > T getAnnotation(Method method, Class<T> annotationClass) {
        return getAnnotation(method.getDeclaringClass(), method, annotationClass);
    }
    
    private static <T extends Annotation > T getAutoDbByInterface(Class<?> cla,Class<T> annotationClass) {
        Class<?> claInter[] = cla.getInterfaces();
        if (claInter != null) {
            for (Class<?> intre : claInter) {
                T t = intre.getAnnotation(annotationClass);
                if (t != null) return t;
            }
        }
        return null;
    }

    private static <T extends Annotation > T getAutoDbByInterfaceMethod(Method method,Class<T> annotationClass) {
        Class<?> cla = method.getDeclaringClass();
        Class<?> claInter[] = cla.getInterfaces();
        if (claInter != null) {
            for (Class<?> intre : claInter) {
                try {
                    Method intreMethod = intre.getMethod(method.getName(), method.getParameterTypes());
                    T t = intreMethod.getAnnotation(annotationClass);
                    if (t != null) return t;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
}
