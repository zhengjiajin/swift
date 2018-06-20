/*
 * @(#)TypeUtil.java   1.0  2014-5-17
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.util.type;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用于处理类型
 * 
 * @author 郑家锦
 * @version 1.0 2014-5-17
 */
public class TypeUtil {

    public final static NumberFormat nf = NumberFormat.getInstance();

    public static String toString(Object obj) {
        if (obj == null) return null;
        if (String.valueOf(obj).trim().length() <= 0) return "";
        return String.valueOf(obj);
    }
    
    public static String toString(Object obj, String def) {
        if (obj == null) return def;
        if (String.valueOf(obj).trim().length() <= 0) return def;
        return String.valueOf(obj);
    }

    public static boolean toBoolen(Object obj) {
        if (TypeUtil.isNull(obj)) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof Number) return ((Number) obj).intValue() > 0 ? true : false;
        return String.valueOf(obj).equalsIgnoreCase("true") ? true : false;
    }

    public static Integer toInt(Object obj) {
        if (TypeUtil.isNull(obj)) return null;
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj) ? 1 : 0;
        }
        return Integer.valueOf(String.valueOf(obj));
    }

    public static int toInt(Object obj, int def) {
        if (TypeUtil.isNull(obj)) return def;
        return toInt(obj);
    }

    public static long toLong(Object obj, long def) {
        if (TypeUtil.isNull(obj)) return def;
        return toLong(obj);
    }

    public static Long toLong(Object obj) {
        if (TypeUtil.isNull(obj)) return null;
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return Long.valueOf(String.valueOf(obj));
    }

    public static Float toFloat(Object obj) {
        if (TypeUtil.isNull(obj)) return null;
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        return Float.valueOf(String.valueOf(obj));
    }

    public static Double toDouble(Object obj) {
        if (TypeUtil.isNull(obj)) return null;
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return Double.valueOf(String.valueOf(obj));
    }

    public static Double toDouble(Object obj, double def) {
        if (TypeUtil.isNull(obj)) return def;
        return toDouble(obj);
    }

    public static Double toDoubleLimit(Object obj, int limit) {
        return toDouble(String.format("%." + limit + "f", toDouble(obj)));
    }

    public static Double toDoubleLimit(Object obj, int limit, double def) {
        if (TypeUtil.isNull(obj)) return def;
        return toDoubleLimit(obj, limit);
    }

    public static Number toNumber(Object obj) {
        if (TypeUtil.isNotNull(obj)) {
            if(obj instanceof Number) return (Number)obj;
            synchronized (nf) {
                try {
                    return nf.parse(String.valueOf(obj));
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (((String) obj).equalsIgnoreCase("null")) return true;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return String.valueOf(obj).trim().isEmpty();
    }
    
    @SuppressWarnings("rawtypes")
    public static int getLength(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof String) {
            return ((String)obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection)obj).size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        return 0;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isTrue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        String booStr = String.valueOf(obj).trim();
        if (booStr.isEmpty()) return false;
        if (booStr.equalsIgnoreCase("true") || booStr.equals("1")) return true;
        return false;
    }

    public static Integer[] toInt(String... str) {
        Integer array[] = new Integer[str.length];
        for (int i = 0; i < str.length; i++) {
            array[i] = Integer.parseInt(str[i]);
        }
        return array;
    }

    public static String[] toStr(Integer... str) {
        String array[] = new String[str.length];
        for (int i = 0; i < str.length; i++) {
            array[i] = String.valueOf(str[i]);
        }
        return array;
    }
    
    public static boolean inList(Object[] list,Object obj) {
        if(list==null || list.length<=0) return false;
        if(obj==null) return false;
        for(Object lobj : list) {
            if(obj.equals(lobj)) return true;
        }
        return false;
    }
    
    public static boolean inList(List<Object> list,Object obj) {
        if(list==null || list.size()<=0) return false;
        if(obj==null) return false;
        for(Object lobj : list) {
            if(obj.equals(lobj)) return true;
        }
        return false;
    }
    
    public static boolean isNumber(Object obj) {
        if (obj == null || "".equals(obj)) {
            return false;
        }
        if(obj instanceof Number) return true;
        return toString(obj).matches("-?[0-9]+.*[0-9]*");
    }
    
    public static boolean isEmail(String email) {     
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";  
        return Pattern.matches(regex, email);     
    } 

}
