/*
 * @(#)BaseJunit4Test.java   1.0  2018年1月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月2日
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(loader = TestContextLoader.class,locations = {"classpath*:*.xml"})
public class BaseJunit4Test extends AbstractJUnit4SpringContextTests {
    protected Log logger = LogFactory.getLog(this.getClass());
}
