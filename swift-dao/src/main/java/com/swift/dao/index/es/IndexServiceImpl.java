/*
 * @(#)IndexServiceImpl.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.parser.DataJsonParser;
import com.swift.core.spring.Spring;
import com.swift.dao.index.es.SearchModel.FieldOrder;
import com.swift.dao.index.es.SearchModel.FilterModel;
import com.swift.dao.index.es.SearchModel.SearchModelOrder;
import com.swift.dao.index.es.factory.ServiceIndexNameFactory;
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

    private SearchRequestBuilder createSearchRequest(SearchModel searchModel) {
        SearchRequestBuilder sb = serviceIndexNameFactory.getClient().prepareSearch(searchModel.getIndexName());
        sb.setTypes(searchModel.getIndexTableName());
        sb.setQuery(createQuery(searchModel));
        sb.setSearchType(SearchType.DEFAULT);
        sb.setFrom(searchModel.getFrom());
        sb.setSize(searchModel.getSize());
        if (searchModel.getSearchModelOrder() != null) {
            for (SearchModelOrder order : searchModel.getSearchModelOrder()) {
                SortOrder so = order.getOrder() == null || order.getOrder().equals(FieldOrder.DESC) ? SortOrder.DESC
                        : SortOrder.ASC;
                sb.addSort(order.getField(), so);
            }
            sb.addSort("_score", SortOrder.DESC);
        }
        HighlightBuilder hb = new HighlightBuilder();
        hb.tagsSchema("default");
        if (searchModel.getHighlightField() != null) {
            for (String hf : searchModel.getHighlightField()) {
                hb.field(hf);
            }
        }
        hb.requireFieldMatch(false);
        sb.highlighter(hb);
        log.info(sb.toString());
        return sb;
    }

    private List<SearchRequestBuilder> createSearchRequest(List<SearchModel> searchModelList) {
        List<SearchRequestBuilder> searchRequestBuilderList = new ArrayList<SearchRequestBuilder>();
        for (SearchModel sm : searchModelList) {
            searchRequestBuilderList.add(createSearchRequest(sm));
        }
        return searchRequestBuilderList;
    }

    // 全文搜索的_score通常会在0到10之间
    private QueryBuilder createQuery(SearchModel searchModel) {
        QueryBuilder qb;
        if (searchModel.getMultiMatch() == null) {
            qb = QueryBuilders.queryStringQuery(searchModel.getContent());
        } else {
            qb = QueryBuilders.multiMatchQuery(searchModel.getContent(), searchModel.getMultiMatch());
        }
        if (searchModel.getFilterField() != null) {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery().must(qb);
            for (FilterModel fm : searchModel.getFilterField()) {
                if (fm != null && fm.getFieldName() != null && fm.getFieldName() != "") {
                    bqb.must(QueryBuilders.termsQuery(fm.getFieldName(), fm.getFieldValue()));
                }
            }
            qb = bqb;
        }
        if (searchModel.getWeightOrderField() != null) {
            FieldValueFactorFunctionBuilder fvf = ScoreFunctionBuilders
                    .fieldValueFactorFunction(searchModel.getWeightOrderField());
            qb = QueryBuilders.functionScoreQuery(qb, fvf).boostMode(CombineFunction.MULTIPLY);
        }
        return qb;
    }

    /**
     * @see com.hhmk.hospital.common.index.IndexService#searchContent(java.lang.String)
     */
    @Override
    public SearchResponseModel searchContent(SearchModel searchModel) {
        SearchRequestBuilder sb = createSearchRequest(searchModel);
        MultiSearchResponse sr = serviceIndexNameFactory.getClient().prepareMultiSearch().add(sb).execute().actionGet();
        return getMultiSearchResponse(sr);
    }

    @Override
    public SearchResponseModel searchContent(List<SearchModel> searchModelList) {
        List<SearchRequestBuilder> searchRequestBuilderList = createSearchRequest(searchModelList);
        MultiSearchRequestBuilder requestBuilder = serviceIndexNameFactory.getClient().prepareMultiSearch();
        for (SearchRequestBuilder sb : searchRequestBuilderList) {
            requestBuilder.add(sb);
        }
        MultiSearchResponse sr = requestBuilder.execute().actionGet();
        return getMultiSearchResponse(sr);
    }

    private String hkey(String key) {
        return SearchResponseModel.HIGHLIGHT_FIELD + "." + key;
    }

    private SearchResponseModel getMultiSearchResponse(MultiSearchResponse multiSearchResponse) {
        SearchResponseModel sm = new SearchResponseModel();
        for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
            if (item.isFailure()) {
                log.error("查询异常", item.getFailure());
                continue;
            }
            SearchResponse response = item.getResponse();
            sm.setSum(sm.getSum() + response.getHits().totalHits());
            sm.setUsetime(sm.getUsetime() + response.getTookInMillis() / 1000f);
            for (SearchHit hit : response.getHits()) {
                try {
                    DataModel datamodel = DataJsonParser.mapToObject(hit.getSource());
                    if (!hit.getHighlightFields().isEmpty()) {
                        for (Entry<String, HighlightField> en : hit.getHighlightFields().entrySet()) {
                            String hkey = hkey(en.getKey());
                            for (Text tt : en.getValue().fragments()) {
                                datamodel.addObject(hkey, tt.string());
                            }
                        }
                    }
                    sm.getData().addObject(hit.getType(), datamodel);
                } catch (Exception ex) {
                    log.error("返回值转JSON异常", ex);
                }
            }
        }
        return sm;
    }

    public static void main(String[] args) {
        SearchModel searchModel = new SearchModel();
        String content = "中心";
        FilterModel filterModel1 = searchModel.new FilterModel();
        filterModel1.setFieldName("status");
        filterModel1.setFieldValue("2");
        FilterModel filterModel2 = searchModel.new FilterModel();
        filterModel2.setFieldName("showList");
        filterModel2.setFieldValue("1");
        FilterModel filterModel3 = searchModel.new FilterModel();
        filterModel3.setFieldName("activityBlock");
        filterModel3.setFieldValue("1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "13");
        searchModel.setFilterField(filterModel1, filterModel2, filterModel3);
        searchModel.setMultiMatch("activityName", "activityIntro", "activityType.typeName");
        searchModel.setContent(content);
        searchModel.setHighlightField("activityName", "activityType.typeName");
        SearchModelOrder searchModelOrder1 = searchModel.new SearchModelOrder();
        searchModelOrder1.setField("weight");
        searchModelOrder1.setOrder(FieldOrder.DESC);
        searchModel.setSearchModelOrder(searchModelOrder1);
        searchModel.setSize(100);
        //searchModel.setWeightOrderField("activityBlock");
        //System.out.println(new IndexServiceImpl().createSearchRequest(searchModel));
        SearchResponseModel sm = Spring.getBean(IndexService.class).searchContent(searchModel);
        System.out.println("sum::::::" + sm.getSum());
        System.out.println(sm.getUsetime());
        for (DataModel dm : sm.getData().getList("*ACTIVITY")) {
            System.out.println("activityId:" + TypeUtil.toInt(dm.getString("activityId")) + ",activityBlock:" + dm.getString("activityBlock") + ",activityName:" + dm.getString("activityName") + ",showList:" + dm.getString("showList") + ",high:" + dm.getString("HIGHLIGHT_FIELD.activityName") + ",activityBlock:" + dm.getString("activityBlock"));
        }
    }

}
