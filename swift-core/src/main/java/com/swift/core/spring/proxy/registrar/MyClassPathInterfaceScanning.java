/*
 * @(#)MyClassPathScanning.java   1.0  2021年7月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月12日
 */
public class MyClassPathInterfaceScanning extends ClassPathScanningCandidateComponentProvider {

    public MyClassPathInterfaceScanning() {
        super(false);
    }
    
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
