/*
 * @(#)SearchModel.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
public class SearchModel {
    /**
     * 需要指定字段搜过
     */
    private String[] multiMatch;
    /**
     * 全文检索内容
     */
    private String content;
    private int from = 0;
    private int size = 100;
    private String[] indexName = new String[] {"swift"};
    private String[] indexTableName = new String[0];
    /**
     * 排序字段
     * 支持aaa.bbb
     */
    private SearchModelOrder[] searchModelOrder;
    /**
     * 过滤掉的字段
     * 支持aaa.bbb
     */
    private FilterModel[] filterField;
    /**
     * 需要高亮的字段
     * 支持aaa.bbb
     */
    private String[] highlightField;
    /**
     * 简单权重字段，排序权重会*此字段
     * 支持aaa.bbb
     */
    private String weightOrderField;

    
    
    /**
     * @return the multiMatch
     */
    public String[] getMultiMatch() {
        return multiMatch;
    }

    /**
     * @param multiMatch the multiMatch to set
     */
    public void setMultiMatch(String... multiMatch) {
        this.multiMatch = multiMatch;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the from
     */
    public int getFrom() {
        return from;
    }

    /**
     * @param from
     *            the from to set
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    

    public String[] getIndexName() {
        return indexName;
    }

    public void setIndexName(String... indexName) {
        this.indexName = indexName;
    }

    public String[] getIndexTableName() {
        return indexTableName;
    }

    public void setIndexTableName(String... indexTableName) {
        this.indexTableName = indexTableName;
    }

    /**
     * @return the searchModelOrder
     */
    public SearchModelOrder[] getSearchModelOrder() {
        return searchModelOrder;
    }

    /**
     * @param searchModelOrder
     *            the searchModelOrder to set
     */
    public void setSearchModelOrder(SearchModelOrder... searchModelOrder) {
        this.searchModelOrder = searchModelOrder;
    }

    /**
     * @return the weightOrderField
     */
    public String getWeightOrderField() {
        return weightOrderField;
    }

    /**
     * @param weightOrderField
     *            the weightOrderField to set
     */
    public void setWeightOrderField(String weightOrderField) {
        this.weightOrderField = weightOrderField;
    }
    
    /**
     * @return the filterField
     */
    public FilterModel[] getFilterField() {
        return filterField;
    }

    /**
     * @param filterField the filterField to set
     */
    public void setFilterField(FilterModel... filterField) {
        this.filterField = filterField;
    }
    
    

    /**
     * @return the highlightField
     */
    public String[] getHighlightField() {
        return highlightField;
    }

    /**
     * @param highlightField the highlightField to set
     */
    public void setHighlightField(String... highlightField) {
        this.highlightField = highlightField;
    }



    public class FilterModel {
        /**
         * 支持aaa.bbb
         */
        private String fieldName;
        
        private Object[] fieldValue;

        /**
         * @return the fieldName
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         * @param fieldName the fieldName to set
         */
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        /**
         * @return the fieldValue
         */
        public Object[] getFieldValue() {
            return fieldValue;
        }

        /**
         * @param fieldValue the fieldValue to set
         */
        public void setFieldValue(Object... fieldValue) {
            this.fieldValue = fieldValue;
        }
        
        
    }
    
    
    public class SearchModelOrder {
        private String field;
        private FieldOrder order;
        /**
         * @return the field
         */
        public String getField() {
            return field;
        }
        /**
         * @param field the field to set
         */
        public void setField(String field) {
            this.field = field;
        }
        /**
         * @return the order
         */
        public FieldOrder getOrder() {
            return order;
        }
        /**
         * @param order the order to set
         */
        public void setOrder(FieldOrder order) {
            this.order = order;
        }
        
    }
    
    public enum FieldOrder {
        ASC,DESC;
    }
}
