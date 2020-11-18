/*
 * @(#)AsynInterface.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.exception.ServiceException;

/**
 * 主服务接口,异步返回接口
 * 实现此接口可使用本包内的所有标签
 * 接口版本控制使用Spring的BEAN名称，BEAN名称统一命名为如：???V1.0 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface AsynInterface extends BaseInterface {

    public void doService(ServiceRequest req,AsynCallBack callBack);
    /**
     * 返回时调用
     * @author DELL
     * @version 1.0 2020年11月18日
     */
    public interface AsynCallBack {
        /*
         * 正常返回
         */
        public void send(DataModel dataModel);
        /*
         * 异常时返回
         */
        public void sendError(ServiceException serviceException);
    }
    
}
