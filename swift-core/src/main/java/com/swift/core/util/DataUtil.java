/*
 * @(#)DataModelUtil.java   1.0  2018年8月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swift.core.model.data.AbstractBeanDataModel;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月7日
 */
public class DataUtil {
    /**
     * 把data字典序排序
     * 
     * @param data
     * @return
     */
    public static DataModel collection(DataModel data) {
        if (data == null) return null;
        DataModel res = new MapDataModel();
        Set<String> keyset = data.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);
        for(String key:list) {
            Object value = data.getObject(key);
            if(value instanceof Map || value instanceof AbstractBeanDataModel) {
                res.addObject(key, collection(data.getDataModel(key)));
            } else {
                res.addObject(key, value);
            }
        }
        return res;
    }
    

    public static void main(String[] args) {
        DataModel req = new MapDataModel();
        req.addObject("bb", 1);
        req.addObject("aa", 2);
        req.addObject("cc.dd", 3);
        req.addObject("cc.aa", 4);
        req.addObject("cc.aa", 5);
        req.addObject("dd", 6);
        System.out.println(req);
        
        System.out.println(collection(req));
        
    }
}
