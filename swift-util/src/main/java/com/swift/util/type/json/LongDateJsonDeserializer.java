/*
 * @(#)LongDateJsonDeserializer.java   1.0  2020年8月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.type.json;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年8月20日
 */
public class LongDateJsonDeserializer extends JsonDeserializer<Date> {

    /** 
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if(p==null) return null;
        if(!TypeUtil.isNumber(p.getValueAsString())) return null;
        long value = p.getLongValue();
        if(TypeUtil.isNull(value)) return null;
        return new Date(value);
    }

}
