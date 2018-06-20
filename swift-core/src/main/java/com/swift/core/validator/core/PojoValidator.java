/*
 * @(#)PojoValidator.java   1.0  2018年6月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.stereotype.Component;

import com.swift.core.model.data.DataModel;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月19日
 */
@Component
public class PojoValidator {
    
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    public List<String> validator(DataModel data) {
        Set<ConstraintViolation<DataModel>> list = validator.validate(data);
        if(TypeUtil.isNull(list)) return null;
        List<String> resList = new LinkedList<String>();
        for(ConstraintViolation<DataModel> viobj : list) {
            resList.add(ValidatorBuilderString.builderString(viobj.getInvalidValue(), viobj.getMessage()));
        }
        return resList;
    }
    
    
}
