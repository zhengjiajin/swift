package com.swift.core.auth;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
/**
 * 普通不需要权限验证的请求 
 * @author zhengjiajin
 * @version 1.0 2018年12月25日
 */
public class AuthRequest {

    // 对端系统ID
    private String toSysId;
	// 调用方法接口
	private String method;

	// 请求参数model
	private DataModel dataModel = new MapDataModel();

    public String getToSysId() {
        return toSysId;
    }

    public void setToSysId(String toSysId) {
        this.toSysId = toSysId;
    }

    public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
