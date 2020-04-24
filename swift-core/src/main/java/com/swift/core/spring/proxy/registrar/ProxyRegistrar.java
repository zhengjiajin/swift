/*
 * @(#)ProxyRegistrar.java   1.0    2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.TypeFilter;

import com.swift.core.spring.proxy.ProxyMapper;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.BeanUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0  2020年4月15日
 */
public class ProxyRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ApplicationContextAware {

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    /**
     * @see org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata,
     *      org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
        if (annotationAttributes == null || annotationAttributes.isEmpty()) return;
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        if (basePackages == null || basePackages.length <= 0) return;
        TypeFilter swiftMapperFilter = new SwiftAnnotationTypeFilter(ProxyMapper.class);
        // TypeFilter helloServiceFilter2 = new AssignableTypeFilter(IMybatisMapper.class);
        MyClassPathBeanDefinitionScanner scanner = new MyClassPathBeanDefinitionScanner(registry);
        scanner.setResourceLoader(this.applicationContext);
        scanner.addIncludeFilter(swiftMapperFilter);
        Set<BeanDefinitionHolder> set = scanner.doScan(basePackages);
        if (set == null) return;
        for (BeanDefinitionHolder beanDefinition : set) {
            String beanClassName = beanDefinition.getBeanDefinition().getBeanClassName();
            String beanName = TypeUtil.toLowerCaseFirstOne(beanClassName.substring(beanClassName.lastIndexOf(".") + 1));
            Class<?> cla = BeanUtil.classForName(beanClassName);
            ProxyMapper proxyMapper = AnnotationUtil.getAnnotation(cla, ProxyMapper.class);
            if(proxyMapper==null) continue;
            BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
            // 注册bean beanName是代理bean的名字 不是RepositoryFactorySupport的名字
            beanDefinitionRegistry.registerBeanDefinition(beanName, createBean(cla));
        }
    }
    /*
     * 创建BEAN描述
     */
    private BeanDefinition createBean(Class<?> cla) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cla);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.getConstructorArgumentValues().addGenericArgumentValue(cla);
        definition.setBeanClass(ProxyServiceFactory.class);
        // 这里采用的是byType方式注入，类似的还有byName等
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        return definition;
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
