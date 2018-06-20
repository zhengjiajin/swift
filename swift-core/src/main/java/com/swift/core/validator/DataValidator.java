/*
 * @(#)Quantity.java   1.0  2018年6月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.validator.annotation.ParamAssertFalse;
import com.swift.core.validator.annotation.ParamAssertTrue;
import com.swift.core.validator.annotation.ParamDecimalMax;
import com.swift.core.validator.annotation.ParamDecimalMin;
import com.swift.core.validator.annotation.ParamDigits;
import com.swift.core.validator.annotation.ParamEmail;
import com.swift.core.validator.annotation.ParamFuture;
import com.swift.core.validator.annotation.ParamLength;
import com.swift.core.validator.annotation.ParamMax;
import com.swift.core.validator.annotation.ParamMin;
import com.swift.core.validator.annotation.ParamNotEmpty;
import com.swift.core.validator.annotation.ParamNotNull;
import com.swift.core.validator.annotation.ParamNull;
import com.swift.core.validator.annotation.ParamPast;
import com.swift.core.validator.annotation.ParamPattern;
import com.swift.core.validator.annotation.ParamRange;
import com.swift.core.validator.annotation.ParamSize;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月15日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataValidator {

    Class<? extends DataModel> value() default MapDataModel.class;

    ParamNull[] Null() default {};

    ParamNotNull[] NotNull() default {};

    ParamAssertTrue[] AssertTrue() default {};

    ParamAssertFalse[] AssertFalse() default {};

    ParamMin[] Min() default {};

    ParamMax[] Max() default {};

    ParamDecimalMin[] DecimalMin() default {};

    ParamDecimalMax[] DecimalMax() default {};

    ParamSize[] Size() default {};

    ParamDigits[] Digits() default {};

    ParamPast[] Past() default {};

    ParamFuture[] Future() default {};

    ParamPattern[] Pattern() default {};

    ParamEmail[] Email() default {};

    ParamLength[] Length() default {};

    ParamNotEmpty[] NotEmpty() default {};

    ParamRange[] Range() default {};
}
