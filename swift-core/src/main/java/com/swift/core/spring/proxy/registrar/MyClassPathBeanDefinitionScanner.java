/*
 * @(#)MyClassPathBeanDefinitionScanner.java   1.0  2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年4月15日
 */
public class MyClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    /**
     * @param registry
     */
    public MyClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry,false);
    }
    
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
       return beanDefinitions;
    }
    
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}