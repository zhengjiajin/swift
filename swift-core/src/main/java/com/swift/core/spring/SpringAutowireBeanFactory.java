/**
 * @(#)SpringAutowireBeanFactory.java 0.0.1-SNAPSHOT Dec 12, 2015 9:33:54 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 自动注入BEAN工厂
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Dec 12, 2015 9:33:54 PM
 */
@Component
public class SpringAutowireBeanFactory implements BeanFactoryAware {

	private AutowireCapableBeanFactory beanFactory;

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (!(beanFactory instanceof AutowireCapableBeanFactory)) {
			throw new IllegalArgumentException("beanFactory can only be an 'AutowireCapableBeanFactory'");
		}
		this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
	}

	public void autowire(Object object) {
		beanFactory.autowireBeanProperties(object, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}
}
