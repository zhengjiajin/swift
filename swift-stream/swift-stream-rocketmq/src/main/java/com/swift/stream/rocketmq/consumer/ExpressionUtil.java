/*
 * @(#)ExpressionUtil.java   1.0  2020年6月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import java.util.List;

import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年6月8日
 */
public class ExpressionUtil {
     public static boolean checkExpression(String[] expressionTags,String tag) {
         if(TypeUtil.isNull(tag)) return true;
        if(TypeUtil.isNull(expressionTags)) return true;
        for(String checkMethod:expressionTags) {
            if("*".equals(checkMethod)) return true;
            if(checkMethod.equalsIgnoreCase(tag)) return true;
            if(checkMethod.startsWith("*")) {
                if(tag.toLowerCase().endsWith(checkMethod.replace("*", "").toLowerCase())) {
                    return true;
                }
            }
            if(checkMethod.endsWith("*")) {
                if(tag.toLowerCase().startsWith(checkMethod.replace("*", "").toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

     public static String subExpression(List<String> expressionTags) {
        if(expressionTags==null || expressionTags.size()<=0) return "*";
        StringBuffer sb = new StringBuffer();
        //tag1 || tag2 || tag3
        for(String tag:expressionTags) {
            if(tag.indexOf("*")!=-1) return "*";
            sb.append(tag).append(" || ");
        }
        return sb.substring(0, sb.length()-4);
    }
     
     public static void main(String[] args) {
        System.out.println("a*adsafs".startsWith("*"));
        System.out.println("a*adsafs".replace("*", ""));
    }
}
