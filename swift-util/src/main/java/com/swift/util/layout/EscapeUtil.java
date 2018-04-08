/*
 * @(#)manageDrNurseList.java   1.0  2015年8月15日
 * 
 * Copyright (c)    2014-2020. All Rights Reserved. GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.layout;

/**
 * 添加说明
 * 
 * @author kyj
 * @version 1.0 2016年06月7日
 */
public class EscapeUtil {

    public static String escapeXml(String str) {
        if(str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            switch(c) {
            case '<':   sb.append("&lt;"); break;
            case '>':   sb.append("&gt;"); break;
            case '"':   sb.append("&quot;"); break;
            case '&':   sb.append("&amp;"); break;
            case '\'':  sb.append("&apos;"); break;
            default:    sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String reverseXml(String str){
        if(str == null) {
            return null;
        }
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        str = str.replace("&quot;", "\"");
        str = str.replace("&amp;", "&");
        str = str.replace("&apos;", "\'");
        return str.toString();
    }
    
    public static String removeTarget(String str) {
        if(str == null) {
            return null;
        }
        str = str.replace("target=\"_blank\"", "");
        str = str.replace("target=\'_blank\'", "");
        return str.toString();
    }
    
    public static String doublequoteToonequote(String str) {
        if(str == null) {
            return null;
        }
        str = str.replace("\"", "\'");
        return str.toString();
    }
}
