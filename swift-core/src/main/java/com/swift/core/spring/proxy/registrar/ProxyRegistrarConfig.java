/*
 * @(#)ProxyRegistrarConfig.java   1.0  2020年1月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年1月10日
 */
@Configuration
@ComponentScan("com.swift")
@Import(ProxyRegistrar.class)
public class ProxyRegistrarConfig {

}
