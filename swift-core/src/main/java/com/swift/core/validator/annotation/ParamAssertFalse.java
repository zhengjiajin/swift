/*
 * @(#)ParamAssertFalse.java   1.0  2018年6月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.annotation;

import javax.validation.constraints.AssertFalse;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月19日
 */
public @interface ParamAssertFalse {
    String param();

    AssertFalse anno() default @AssertFalse;
}
