/*
 * @(#)ProxyAnnotationBeanNameGenerator.java   1.0  2020年4月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年4月26日
 */
public class ProxyAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {


    private static String IMPL_BEAN_NAME="ProxyImpl";
    
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return super.generateBeanName(definition, registry)+IMPL_BEAN_NAME;
    }
}
