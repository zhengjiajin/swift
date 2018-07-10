/*
 * @(#)IndexService.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es;


import java.util.List;

/**
 * 搜索引擎的业务查询接口
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
public interface IndexService {
    /**
     * 模糊查询结果集
     * 默认返回100条
     *
     * @return
     */
    public SearchResponseModel searchContent(SearchModel searchModel);

    public SearchResponseModel searchContent(List<SearchModel> searchModelList);

}
