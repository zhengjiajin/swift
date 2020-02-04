/*
 * @(#)DataModel.java   1.0  2016年4月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.data;

import java.util.List;
import java.util.Set;

import com.swift.util.type.TypeUtil;

/**
 * 交互传输时使用到的共用方法接口类
 * 能使用各种规则取得与插入相应的数据
 * objectPath格式为：树状结构的目录名称
 * objectName：普通结构
 * objectName[N]：代表LIST结构的第N位
 * *objectName：代码此值为LIST结构
 * 
 * 树状结构可如：插入只支持一层*级
 * objectName1.objectName2.objectName3
 * objectName1.objectName2.*objectName3
 * objectName1.*objectName2.objectName3
 * *objectName1.objectName2.objectName3
 * objectName1.objectName2[1].objectName3
 * objectName1[1].objectName2.objectName3[2]
 * 等
 * 
 * 
 * 实现方式按向下层递归的方式
 * objectName1.objectName2.objectName3-》objectName2.objectName3-》objectName3
 * 
 * 
 * @author zhengjiajin
 * @version 1.0 2016年4月26日
 */
public interface DataModel {
    /**
     * 向objectPath里插入一个值
     * 重复则转为List不覆盖，值放置于List的最后一位
     * 默认为MAP的实现
     * 如BeanDataModel无此方法则
     * @param objectPath 参照objectPath的说明
     * @param value 非基础类型必须实现DataModel
     */
    public void addObject(String objectPath, Object value);
    /**
     * 向objectPath里插入一个值,objectPath的对象为List
     * 新建方法时默认为MAP的实现
     * 如BeanDataModel无此方法则
     * @param objectPath 参照objectPath的说明
     * @param index List的指针
     * @param value 非基础类型必须实现DataModel
     */
    public void addObject(String objectPath, int index, Object value);
    /**
     * 向objectPath里设置一个值
     * 重复则覆盖
     * 如BeanDataModel无此方法则
     * @param objectPath 参照objectPath的说明
     * @param value 非基础类型必须实现DataModel
     */
    public void putObject(String objectPath, Object value);
    /**
     * 得到一个DataModel对象值
     * 非此对象会抛出RuntimeException
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public DataModel getDataModel(String objectPath);

    /**
     * 得到一个对象值
     * @param objectPath
     * @return
     * @throws RuntimeException
     */
    public Object getObject(String objectPath);

    /**
     * 得到结果值并转为T
     * 如结果值为MapDataModel
     * 如结果值为基础类型则抛出;
     * @param objectPath 参照objectPath的说明
     * @param classType
     * @return
     */
    public <T extends DataModel> T getObject(String objectPath, Class<T> classType);
    /**
     * 得到结果值并转为List<DataModel>
     * 如结果值为非List类型，则转为List类型
     * 如结果值为基础类型则抛出;
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<DataModel> getList(String objectPath);
    /**
     * 得到结果值并转为List<T>
     * 如结果值为非List类型，则转为List类型
     * 如结果值为基础类型则抛出;
     * @param objectPath 参照objectPath的说明
     * @param classType
     * @return
     */
    public <T extends DataModel> List<T> getList(String objectPath, Class<T> classType);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public String getString(String objectPath);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public Long getLong(String objectPath);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public Integer getInteger(String objectPath);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public Float getFloat(String objectPath);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public Double getDouble(String objectPath);
    /**
     * 得到结果值
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public Boolean getBoolean(String objectPath);
    
    /**
     * 得到List类型值
     * @param objectPath
     * @return
     * @throws RuntimeException
     */
    public List<Object> getListObject(String objectPath);
    /**
     * 得到结果值并转为List
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<String> getListString(String objectPath);
    /**
     * 得到结果值并转为List
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<Integer> getListInteger(String objectPath);
    /**
     * 得到结果值并转为List
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<Double> getListDouble(String objectPath);
    /**
     * 得到结果值并转为List
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<Float> getListFloat(String objectPath);
    /**
     * 得到结果值并转为List
     * @param objectPath 参照objectPath的说明
     * @return
     */
    public List<Boolean> getListBoolean(String objectPath);
    /**
     * 删除结果值，如objectPath带下标[N]则只删除下标的数据
     * @param objectPath 参照objectPath的说明
     */
    public void remove(String objectPath);
    
    /**
     * 返回当前类的KEY
     * @return
     */
    Set<String> keySet();
    
    static final String EMPTY = "";
	static final String DOT = ".";
	static final String ASTERISK = "*";
	static final String NUMBER_REGEX = "\\[\\d*\\]";
	
	
    public default String getString(String objectPath, String def) {
        return TypeUtil.toString(getString(objectPath), def);
    }

    public default long getLong(String objectPath, long def) {
        return TypeUtil.toLong(getLong(objectPath), def);
    }

    public default int getInteger(String objectPath, int def) {
        return TypeUtil.toInt(getInteger(objectPath), def);
    }

    public default float getFloat(String objectPath, float def) {
        return TypeUtil.toFloat(getFloat(objectPath),def);
    }

    public default double getDouble(String objectPath, double def) {
        return TypeUtil.toDouble(getDouble(objectPath), def);
    }
}
