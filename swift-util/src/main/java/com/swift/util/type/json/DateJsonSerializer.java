/*
 * @(#)DateJsonSerializer.java   1.0  2020年8月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.type.json;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.swift.util.date.DateUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年8月19日
 */
public class DateJsonSerializer extends JsonSerializer<Date> {

    /** 
     * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
     */
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException, JsonProcessingException {
        if(value==null) return;
        gen.writeString(DateUtil.formatDate(value));
    }

}
