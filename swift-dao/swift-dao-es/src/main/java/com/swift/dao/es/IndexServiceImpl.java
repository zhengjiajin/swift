/*
 * @(#)IndexServiceImpl.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.parser.DataJsonParser;
import com.swift.dao.es.factory.ServiceIndexNameFactory;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Service
public class IndexServiceImpl implements IndexService {
    private final static Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private ServiceIndexNameFactory serviceIndexNameFactory;

    @Value("${es.index_dev}")
    private String INDEX_DEV;

    private static final String DEF_TYPE = "_doc";

    private String getIndex(String index) {
        if (TypeUtil.isNull(INDEX_DEV)) return index;
        return INDEX_DEV + index;
    }

    /**
     * @see com.swift.plug.es.IndexService#get(java.lang.String, java.lang.String)
     */
    @Override
    public DataModel get(String index, String key) {
        GetRequest getRequest = new GetRequest(getIndex(index), DEF_TYPE, key);
        try {
            GetResponse getResponse = serviceIndexNameFactory.getClient().get(getRequest,
                ServiceIndexNameFactory.COMMON_OPTIONS);
            String json = getResponse.getSourceAsString();
            if (TypeUtil.isNull(json)) return null;
            return DataJsonParser.jsonToObject(json);
        } catch (IOException e) {
            log.error("查询ES失败:", e);
            serviceIndexNameFactory.restart();
        }
        return null;
    }

    /**
     * @see com.swift.plug.es.IndexService#searchContent(org.elasticsearch.action.search.SearchRequest)
     */
    @Override
    public List<DataModel> searchContent(String index, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(getIndex(index));
        searchRequest.types(DEF_TYPE);
        searchRequest.source(searchSourceBuilder);
        List<DataModel> list = new ArrayList<>();
        try {
            SearchResponse searchResponse = serviceIndexNameFactory.getClient().search(searchRequest,
                ServiceIndexNameFactory.COMMON_OPTIONS);
            SearchHits hits = searchResponse.getHits();
            if(hits==null) return null;
            hits.forEach(new Consumer<SearchHit>() {
                @Override
                public void accept(SearchHit t) {
                    String json = t.getSourceAsString();
                    if(TypeUtil.isNull(json)) return;
                    list.add(DataJsonParser.jsonToObject(json));
                }

            });

        } catch (IOException e) {
            log.error("查询ES失败:", e);
            serviceIndexNameFactory.restart();
        }
        return list;
    }

}
