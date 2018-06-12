/*
 * @(#)SearchResponseModel.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
public class SearchResponseModel {
    /**
     * 处理过的高亮字段存方地址
     */
    public final static String HIGHLIGHT_FIELD="HIGHLIGHT_FIELD";
    /**
     * 条数
     */
    private long sum = 0;
    /**
     * 使用时间
     */
    private float usetime=0f;
    /**
     * 数据
     */
    private DataModel data=new MapDataModel();
    /**
     * @return the sum
     */
    public long getSum() {
        return sum;
    }
    /**
     * @param sum the sum to set
     */
    public void setSum(long sum) {
        this.sum = sum;
    }
    /**
     * @return the usetime
     */
    public float getUsetime() {
        return usetime;
    }
    /**
     * @param usetime the usetime to set
     */
    public void setUsetime(float usetime) {
        this.usetime = usetime;
    }
    /**
     * @return the data
     */
    public DataModel getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(DataModel data) {
        this.data = data;
    }
    
    
}
