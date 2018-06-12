/*
 * @(#)BaseJunit4Test.java   1.0  2018年1月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月2日
 */
@ContextConfiguration(loader = TestContextLoader.class)
public class BaseJunit4Test extends AbstractJUnit4SpringContextTests {
    protected Log logger = LogFactory.getLog(this.getClass());
}
