/*
 * @(#)TestIndex.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.index;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.index.es.IndexService;
import com.swift.dao.index.es.SearchModel;
import com.swift.dao.index.es.SearchResponseModel;
import com.swift.test.BaseJunit4Test;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public class TestIndex extends BaseJunit4Test{
    @Autowired
    private IndexService indexService;
    
    @Test
    public void testIndex() {
        SearchModel searchModel = new SearchModel();
        searchModel.setContent("DHA");
        searchModel.setIndexTableName("ACTIVITY");
        searchModel.setIndexName("hhmk");
        SearchResponseModel res = indexService.searchContent(searchModel);
        System.out.println(res.getData());
    }
}
