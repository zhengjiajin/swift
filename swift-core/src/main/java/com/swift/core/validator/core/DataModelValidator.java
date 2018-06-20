/*
 * @(#)IntrefaceValidator.java   1.0  2018年6月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.swift.core.model.data.DataModel;
import com.swift.core.validator.DataValidator;
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
import com.swift.core.validator.core.data.param.ParamAssertFalseDef;
import com.swift.core.validator.core.data.param.ParamAssertTrueDef;
import com.swift.core.validator.core.data.param.ParamDecimalMaxDef;
import com.swift.core.validator.core.data.param.ParamDecimalMinDef;
import com.swift.core.validator.core.data.param.ParamDigitsDef;
import com.swift.core.validator.core.data.param.ParamEmailDef;
import com.swift.core.validator.core.data.param.ParamFutureDef;
import com.swift.core.validator.core.data.param.ParamLengthDef;
import com.swift.core.validator.core.data.param.ParamMaxDef;
import com.swift.core.validator.core.data.param.ParamMinDef;
import com.swift.core.validator.core.data.param.ParamNotEmptyDef;
import com.swift.core.validator.core.data.param.ParamNotNullDef;
import com.swift.core.validator.core.data.param.ParamNullDef;
import com.swift.core.validator.core.data.param.ParamPastDef;
import com.swift.core.validator.core.data.param.ParamPatternDef;
import com.swift.core.validator.core.data.param.ParamRangeDef;
import com.swift.core.validator.core.data.param.ParamSizeDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月19日
 */
@Component
public class DataModelValidator {
    
    public List<String> validator(DataValidator dataValidator,DataModel data) {
        List<String> list = new LinkedList<String>();
        
        for(ParamNull p:dataValidator.Null()) {
            String error = ParamNullDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
            
        }
        
        for(ParamNotNull p:dataValidator.NotNull()) {
            String error = ParamNotNullDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamAssertTrue p:dataValidator.AssertTrue()) {
            String error = ParamAssertTrueDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamAssertFalse p:dataValidator.AssertFalse()) {
            String error = ParamAssertFalseDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamMin p:dataValidator.Min()) {
            String error = ParamMinDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamMax p:dataValidator.Max()) {
            String error = ParamMaxDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamDecimalMin p:dataValidator.DecimalMin()) {
            String error = ParamDecimalMinDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamDecimalMax p:dataValidator.DecimalMax()) {
            String error = ParamDecimalMaxDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamSize p:dataValidator.Size()) {
            String error = ParamSizeDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamDigits p:dataValidator.Digits()) {
            String error = ParamDigitsDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamPast p:dataValidator.Past()) {
            String error = ParamPastDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamFuture p:dataValidator.Future()) {
            String error = ParamFutureDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamPattern p:dataValidator.Pattern()) {
            String error = ParamPatternDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamEmail p:dataValidator.Email()) {
            String error = ParamEmailDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamLength p:dataValidator.Length()) {
            String error = ParamLengthDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamNotEmpty p:dataValidator.NotEmpty()) {
            String error = ParamNotEmptyDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        for(ParamRange p:dataValidator.Range()) {
            String error = ParamRangeDef.getDef().valueDef(data, p);
            if(TypeUtil.isNotNull(error)) list.add(error);
        }
        
        if(TypeUtil.isNull(list)) return null;
        return list;
    }
    
}
