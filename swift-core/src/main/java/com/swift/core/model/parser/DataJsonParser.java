/*
 * @(#)DataJsonParser.java   1.0  2015年8月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.parser;

import java.util.List;
import java.util.Map;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.util.type.JsonUtil;

/**
 * JSON处再加一层转码，让其支持DataModel
 * @author zhengjiajin
 * @version 1.0 2015年8月20日
 */
public class DataJsonParser {

    public static DataModel jsonToObject(String json) {
        return mapToObject(JsonUtil.toObj(json, Map.class));
    }
    
    public static DataModel mapToObject(Map<?, ?> map) {
        DataModel model = new MapDataModel();
        parseJson(map, model);
        return model;
    }
    
    
    private static void parseJson(Map<?, ?> map, DataModel model) {
        for (Object key : map.keySet()) {
            Object obj = map.get(key);
            Object object;
            if (obj instanceof Map) {
                object = new MapDataModel();
                parseJson((Map<?, ?>) obj, (DataModel) object);
                model.addObject((String)key,object);
            } else if (obj instanceof List) {
                List<?> array = (List<?>) obj;
                for (int i = 0; i < array.size(); ++i) {
                    if (array.get(i) instanceof Map) {
                        object = new MapDataModel();
                        parseJson((Map<?, ?>) array.get(i), (DataModel) object);
                        model.addObject("*" + (String)key,object);
                    } else {
                        object = array.get(i);
                        model.addObject("*" + (String)key,object);
                    }
                }
            } else {
                model.addObject((String)key,obj);
            }
        }
    }
}
