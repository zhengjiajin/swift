/*
 * @(#)GetModel.java   1.0  2018年6月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.basehttp.serviceV1_1;

import com.swift.core.model.data.IBaseModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月12日
 */
public class GetModel extends IBaseModel{
    private Integer mapx;
    
    private String mapy;

    public Integer getMapx() {
        return mapx;
    }

    public void setMapx(Integer mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }
    
}
