/*
 * @(#)AnnotationTypeUtil.java   1.0  2021年7月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.filter.TypeFilter;

import com.swift.core.spring.proxy.registrar.MyClassPathInterfaceScanning;
import com.swift.core.spring.proxy.registrar.SwiftAnnotationTypeFilter;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月22日
 */
public class AnnotationTypeUtil {

    private static Map<Class<? extends Annotation>,List<String>> ANNOTATION_CLA_MAP = new ConcurrentHashMap<>();
    
    public static List<String> getAnnotationClassList(Class<? extends Annotation> annotationCla){
        if(ANNOTATION_CLA_MAP.containsKey(annotationCla)) return ANNOTATION_CLA_MAP.get(annotationCla);
        List<String> list = new ArrayList<>();
        TypeFilter filter = new SwiftAnnotationTypeFilter(annotationCla);
        MyClassPathInterfaceScanning scan = new MyClassPathInterfaceScanning();
        scan.addIncludeFilter(filter);
        Set<BeanDefinition> beans= scan.findCandidateComponents("com.swift");
        if(beans==null || beans.isEmpty()) {
            ANNOTATION_CLA_MAP.put(annotationCla, list);
            return list;
        }
        for(BeanDefinition bean:beans){
            String beanClassName = bean.getBeanClassName();
            list.add(beanClassName);
        }
        ANNOTATION_CLA_MAP.put(annotationCla, list);
        return list;
    }
}
