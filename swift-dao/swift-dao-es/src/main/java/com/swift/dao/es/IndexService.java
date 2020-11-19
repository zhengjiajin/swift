/*
 * @(#)IndexService.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es;


import java.util.List;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.swift.core.model.data.DataModel;

/**
 * 搜索引擎的业务查询接口
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
public interface IndexService {
    
    public DataModel get(String index,String key);
    
    public List<DataModel> searchContent(String index, SearchSourceBuilder searchSourceBuilder);

}
