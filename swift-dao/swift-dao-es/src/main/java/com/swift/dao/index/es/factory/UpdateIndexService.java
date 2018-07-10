/*
 * @(#)UpdateIndexService.java   1.0  2016年11月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es.factory;

import java.util.List;

import com.swift.core.model.data.DataModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年11月29日
 */
public interface UpdateIndexService {
      public List<ReturnIndexModel> updateKey(String... keys);
      
      public class ReturnIndexModel{
         private String key;
         private DataModel dataModel;
        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }
        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }
        /**
         * @return the dataModel
         */
        public DataModel getDataModel() {
            return dataModel;
        }
        /**
         * @param dataModel the dataModel to set
         */
        public void setDataModel(DataModel dataModel) {
            this.dataModel = dataModel;
        }
         
      }
}
