/*
 * @(#)UpdateIndexService.java   1.0  2016年11月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es.factory;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2016年11月29日
 */
public interface UpdateIndexService {

    public List<IndexSourceModel> updateKey(String... ids);
    
    public String getKey(Object id);

    public abstract class IndexSourceModel extends AbstractBeanDataModel {

        @JsonIgnoreProperties
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
        
        public abstract String getId();

    }

    public class IntegerRange extends AbstractBeanDataModel {
        //小值
        private Integer gte;
        //大值
        private Integer lte;
        
        public IntegerRange(Integer gte,Integer lte) {
            this.gte=gte;
            this.lte=lte;
        }

        public Integer getGte() {
            return gte;
        }

        public void setGte(Integer gte) {
            this.gte = gte;
        }

        public Integer getLte() {
            return lte;
        }

        public void setLte(Integer lte) {
            this.lte = lte;
        }

    }
}
