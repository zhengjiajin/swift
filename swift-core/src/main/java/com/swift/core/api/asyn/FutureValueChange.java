/*
 * @(#)FutureValueChange.java   1.0  2020年9月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api.asyn;

/**
 *结果值转换 
 * @author zhengjiajin
 * @version 1.0 2020年9月17日
 */
public interface FutureValueChange<T,V> {

    public V toChange(T t);
}
