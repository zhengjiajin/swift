/*
 * @(#)DataValidatorAop.java   1.0  2018年6月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.AbstractBeanDataModel;
import com.swift.core.model.data.DataModel;
import com.swift.core.validator.DataValidator;
import com.swift.core.validator.core.DataModelValidator;
import com.swift.core.validator.core.PojoValidator;
import com.swift.core.validator.core.ValidatorBuilderString;
import com.swift.exception.SwiftRuntimeException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月15日
 */
@Aspect
@Component
public class DataValidatorAop {

    private static final Logger log = LoggerFactory.getLogger(DataValidatorAop.class);

    public static final String FILTER_STR = "@within(com.swift.core.validator.DataValidator) "
        + " || @annotation(com.swift.core.validator.DataValidator) ";

    @Autowired
    private PojoValidator pojoValidator;

    @Autowired
    private DataModelValidator dataModelValidator;

    @Around(DataValidatorAop.FILTER_STR)
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Class<?> cla = pjp.getTarget().getClass();
        DataValidator dataValidator = method.getAnnotation(DataValidator.class);
        if (dataValidator == null) {
            dataValidator = cla.getAnnotation(DataValidator.class);
        }
        if (dataValidator != null) {
            check(dataValidator, pjp.getArgs());
        }
        pjp.proceed(pjp.getArgs());
    }

    private void check(DataValidator dataValidator, Object[] obj) {
        if (obj == null) return;
        if (obj.length <= 0) return;
        if (!(obj[0] instanceof ServiceRequest)) return;
        ServiceRequest req = (ServiceRequest) obj[0];
        if (req.getData() == null) return;
        if (AbstractBeanDataModel.class.isAssignableFrom(dataValidator.value()) && !(req.getData() instanceof AbstractBeanDataModel)) {
            req.setData(AbstractBeanDataModel.mapToBean(req.getData(), dataValidator.value()));
        }
        String str = validator(dataValidator, req.getData());
        if (TypeUtil.isNotNull(str)) {
            log.warn(str);
            throw new SwiftRuntimeException(str);
        }
    }

    private String validator(DataValidator dataValidator, DataModel data) {
        List<String> list = null;
        if (data instanceof AbstractBeanDataModel) {
            list = pojoValidator.validator(data);
        } else {
            list = dataModelValidator.validator(dataValidator, data);
        }
        if (TypeUtil.isNull(list)) return null;
        return ValidatorBuilderString.builderString(list);
    }
}
