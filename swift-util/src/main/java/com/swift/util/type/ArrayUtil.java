package com.swift.util.type;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 数组操作util
 * 
 * @author zhangxin
 * 2015-1-13
 */
public class ArrayUtil {

	
	/**
     * 合并两个数组
     * @param array1
     * @param array2
     * @return
     */
    public static Object[] joinArray(Object[] array1, Object[] array2) {
    	Object result[] = new Object[array1.length+array2.length];
    	System.arraycopy(array1, 0, result, 0, array1.length);
    	System.arraycopy(array2, 0, result, array1.length, array2.length);
    	return result;
    }

	/**
	 * Convert a string like 1,2,3,4 into an integer list
	 *
	 * @param str	a string like 1,2,3,4
	 * @return
	 */
	public static List<Integer> toList(String str){
		String[] parts = str.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(String part:parts){
			list.add(Integer.parseInt(part));
		}
		return list;
	}
	/**
	 * 
	 * @param list
	 * @return a string like 1,2,3,4
	 */
	public static String toString(List<?> list){
	    StringBuffer sb = new StringBuffer();
        for(Object obj:list){
            sb.append(TypeUtil.toString(obj,"")).append(",");
        }
        if(sb.length()>0)
        return sb.substring(0, sb.length()-1);
        else return "";
	}
	
    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}
