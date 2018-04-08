/*
 * @(#)DataModelUtil.java 1.0.0 May 3, 2016 8:36:22 PM 
 *  
 * Copyright (c) 2015-2016 GuangZhou hhmk Technology Company LTD.  All rights reserved. 
 */
package com.swift.core.model;

import java.util.List;

/**
 * TODO Purpose/description of class
 * 
 * @author zhengjiajin
 * @version 1.0.0
 * @date May 3, 2016 8:36:22 PM
 */
public class DataModelUtil {

	private DataModelUtil() {
		// Do nothing
	}

	/**
	 * 去除字符串中的*号
	 * 
	 * @param src
	 * @return
	 */
	public static String stripAsterisk(String src) {
		if (src == null) {
			return null;
		}
		return src.replace(DataModel.ASTERISK, DataModel.EMPTY);
	}

	public static String stripSubscripting(String src) {
		if (src == null) {
			return null;
		}
		return src.replaceAll(DataModel.NUMBER_REGEX, DataModel.EMPTY);
	}

	public static String normalize(String src) {
		if (src == null) {
			return null;
		}
		src = stripAsterisk(src);
		return stripSubscripting(src);
	}

	public static String nextToken(String path) {
		int idx = path.indexOf(DataModel.DOT);
		if (idx == -1) {
			return path;
		}
		return path.substring(0, idx);
	}

	public static int getIndex(String name) {
		int start = name.indexOf('[');
		int end = name.indexOf(']');
		if (start >= 0 && end >= 0 && end > start) {
			String n = name.substring(start + 1, end);
			if ("".equals(n)) {
				return 0;
			}
			try {
				return Integer.parseInt(n);
			} catch (NumberFormatException ex) {
			}
		}
		return -1;
	}

	public static void throwExceptionStartWithAsterisk(String src) {
		if (src == null) {
			return;
		}
		if (src.startsWith(DataModel.ASTERISK)) {
			throw new RuntimeException("Type mismatch '" + src + "' is not a java.lang.List");
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			if (((String) obj).equalsIgnoreCase("null"))
				return true;
		}
		if (obj instanceof List) {
			return ((List) obj).isEmpty();
		}
		return String.valueOf(obj).trim().isEmpty();
	}

	public static boolean objToBoolen(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue() > 0 ? true : false;
		}
		return String.valueOf(obj).equalsIgnoreCase("true") ? true : false;
	}

	public static Integer objToInt(Object obj) {
		if (isNull(obj)) {
			return null;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		}
		if (obj instanceof Boolean) {
			return ((Boolean) obj) ? 1 : 0;
		}
		return Integer.valueOf(String.valueOf(obj));
	}

	public static Float objToFloat(Object obj) {
		if (isNull(obj)) {
			return null;
		}
		if (obj instanceof Number) {
			return ((Number) obj).floatValue();
		}
		return Float.valueOf(String.valueOf(obj));
	}

	public static Double objToDouble(Object obj) {
		if (isNull(obj)) {
			return null;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue();
		}
		return Double.valueOf(String.valueOf(obj));
	}

	public static Long objToLong(Object obj) {
		if (isNull(obj)) {
			return null;
		}
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		}
		return Long.valueOf(String.valueOf(obj));
	}

	public static String objToString(Object obj) {
		if (obj == null) {
			return null;
		}
		return String.valueOf(obj);
	}
}
