/*
 * @(#)TestDataModel.java   1.0  2019年4月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.test;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.util.text.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月2日
 */
public class TestDataModel {

   
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        DataModel data = new MapDataModel();
        data.addObject("str", "topstr");
        data.addObject("*list", "list1");
        data.addObject("*list", "list2");
        data.addObject("acObj.objOneStr", "objOneStr1");
        data.addObject("acObj.objTwo.objTwostr", "objTwostrone");
        data.addObject("acObj.objTwo.*objTwostrList", "objTwostrList1");
        data.addObject("acObj.objTwo.*objTwostrList", "objTwostrList2");
        
        /*data.addObject("*acList.objOneStr", "acListobjOneStr1");
        data.addObject("*acList.objTwo.objTwostr", "objTwoobjTwostr1");
        data.addObject("*acList.objTwo.*objTwostrList", "objTwoobjTwostr1objTwostrList1");
        data.addObject("*acList.objTwo.*objTwostrList", "objTwoobjTwostr1objTwostrList2");
        
        data.addObject("*acList.objOneStr", "acListobjOneStr2");
        data.addObject("*acList.objTwo.objTwostr", "objTwoobjTwostr2");
        data.addObject("*acList.objTwo.*objTwostrList", "objTwoobjTwostr2objTwostrList1");
        data.addObject("*acList.objTwo.*objTwostrList", "objTwoobjTwostr2objTwostrList2");*/
        DataModel acList = new MapDataModel();
        acList.addObject("objOneStr", "acListobjOneStr1");
        acList.addObject("objTwo.objTwostr", "objTwoobjTwostr1");
        acList.addObject("objTwo.*objTwostrList", "objTwoobjTwostr1objTwostrList1");
        acList.addObject("objTwo.*objTwostrList", "objTwoobjTwostr1objTwostrList2");
        data.addObject("acList", acList);
        acList = new MapDataModel();
        acList.addObject("objOneStr", "acListobjOneStr2");
        acList.addObject("objTwo.objTwostr", "objTwoobjTwostr2");
        acList.addObject("objTwo.*objTwostrList", "objTwoobjTwostr2objTwostrList1");
        acList.addObject("objTwo.*objTwostrList", "objTwoobjTwostr2objTwostrList2");
        data.addObject("acList", acList);
        
        /*{
            "acList": [{
                "objTwo": {
                    "objTwostr": "objTwoobjTwostr1",
                    "objTwostrList": ["objTwoobjTwostr1objTwostrList2", "objTwoobjTwostr1objTwostrList1"]
                },
                "objOneStr": "acListobjOneStr1"
            }, {
                "objTwo": {
                    "objTwostr": "objTwoobjTwostr2",
                    "objTwostrList": ["objTwoobjTwostr2objTwostrList2", "objTwoobjTwostr2objTwostrList1"]
                },
                "objOneStr": "acListobjOneStr2"
            }],
            "list": ["list2", "list1"],
            "acObj": {
                "objTwo": {
                    "objTwostr": "objTwostrone",
                    "objTwostrList": ["objTwostrList2", "objTwostrList1"]
                },
                "objOneStr": "objOneStr1"
            },
            "str": "topstr"
        }*/
        System.out.println("输出总数:"+JsonUtil.toJson(data));
        System.out.println("获取单值:"+data.getString("str")+"应输出:topstr");
        System.out.println("获取LIST:"+data.getListString("list")+"应输出:[\"list2\", \"list1\"]");
        System.out.println("获取对象:"+JsonUtil.toJson(data.getDataModel("acObj")));
        System.out.println("获取对象里的某个值:"+data.getObject("acObj.objOneStr"));
        System.out.println("获取对象里的对象:"+JsonUtil.toJson(data.getDataModel("acObj.objTwo")));
        System.out.println("获取对象里对象的某个值:"+data.getObject("acObj.objTwo.objTwostr"));
        System.out.println("获取对象里对象的某个LIST值:"+data.getObject("acObj.objTwo.objTwostrList"));
        System.out.println("获取LIST对象:"+JsonUtil.toJson(data.getObject("acList")));
        System.out.println("获取LIST对象的首对象:"+JsonUtil.toJson(data.getDataModel("acList")));
        System.out.println("获取LIST对象的某个属性值:"+data.getObject("acList.objTwo.objTwostr")+"应输出:[\"acListobjOneStr1\",\"acListobjOneStr2\"]");
        System.out.println("获取LIST对象的某个LIST属性值:"+data.getObject("acList.objTwo.objTwostrList"));
        System.out.println("获取简单LIST对象下标:"+data.getObject("list[1]"));
        System.out.println("获取多级LIST对象下标:"+data.getObject("acList[0].objTwo.objTwostrList[1]"));
        System.out.println("获取多级LIST对象下标删前0:"+JsonUtil.toJson(data.getObject("acList[0].objTwo")));
        System.out.println("获取多级LIST对象下标删前1:"+JsonUtil.toJson(data.getObject("acList[1].objTwo")));
        data.remove("acList[0].objTwo.objTwostrList[1]");
        System.out.println("获取多级LIST对象下标0:"+JsonUtil.toJson(data.getObject("acList[0].objTwo")));
        System.out.println("获取多级LIST对象下标1:"+JsonUtil.toJson(data.getObject("acList[1].objTwo")));
        System.out.println(data.getObject("acList[0].objTwo.objTwostrList[0]"));
        System.out.println(data.getObject("acList[1].objTwo.objTwostrList[1]"));
        data.remove("acObj");
        System.out.println("输出总数:"+JsonUtil.toJson(data));
    }
    
}
