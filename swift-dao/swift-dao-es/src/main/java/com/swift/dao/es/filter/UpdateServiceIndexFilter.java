/*
 * @(#)UpdateServiceIndexFilter.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.es.filter;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.swift.core.filter.ResponseFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.processor.ReqInterfaceFactory;
import com.swift.core.spring.Spring;
import com.swift.dao.es.annontation.ServiceIndex;
import com.swift.dao.es.annontation.ServiceIndex.Tnteraction;
import com.swift.dao.es.factory.ServiceIndexNameFactory;
import com.swift.dao.es.factory.UpdateIndexService;
import com.swift.dao.es.factory.UpdateIndexService.IndexSourceModel;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Service
@Order(1)
public class UpdateServiceIndexFilter implements ResponseFilter {

    private final static Logger log = LoggerFactory.getLogger(UpdateServiceIndexFilter.class);

    @Value("${es.index_dev}")
    private String INDEX_DEV;

    @Autowired
    private ServiceIndexNameFactory serviceIndexNameFactory;

    /**
     * @see com.hhmk.hospital.common.filter.ResponseFilter#doFilter(com.ServiceResponse.hospital.common.model.ServerResponse)
     */
    @Override
    public void doFilter(ServiceResponse res) {
        if (res.getResultCode() != 0) return;
        ServiceRequest req = res.getRequest();
        Object obj = ReqInterfaceFactory.getInterfaceTrueObj(req.getMethod(), req.getInterfaceVersion());
        ServiceIndex serviceIndex = AnnotationUtil.getAnnotation(obj.getClass(), ServiceIndex.class);
        if (serviceIndex == null) return;
        UpdateIndexService updateIndexService = (UpdateIndexService) Spring.getBean(serviceIndex.updateIndexClassName());
        if (updateIndexService == null) return;
        String[] ids = getIds(serviceIndex, res);
        if(ids==null) return;
        List<IndexSourceModel> rims = updateIndexService.updateKey(ids);
        doIndex(updateIndexService,serviceIndex, rims, ids);
    }

    private String[] getIds(ServiceIndex serviceIndex, ServiceResponse res) {
        DataModel dm = null;
        if (serviceIndex.keyTnteraction().equals(Tnteraction.request)) {
            dm = res.getRequest().getData();
        } else if (serviceIndex.keyTnteraction().equals(Tnteraction.response)) {
            dm = res.getData();
        }
        List<String> ids = dm.getListString(serviceIndex.primaryPath());
        if (ids == null || ids.isEmpty()) return null;
        String[] sss = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            sss[i] = ids.get(i);
        }
        return sss;
    }

    private void doIndex(UpdateIndexService updateIndexService, ServiceIndex serviceIndex, List<IndexSourceModel> rims, String... ids) {
        // 删除操作
        if (ids == null) return;
        for (String id : ids) {
            if (noExists(rims, id)) {
                DeleteRequest deleteRequest = new DeleteRequest(getIndex(serviceIndex), serviceIndex.type(),updateIndexService.getKey(id));
                boolean isupdate = deleteIndex(deleteRequest);
                if (!isupdate) {
                    // 重试一次
                    serviceIndexNameFactory.restart();
                    deleteIndex(deleteRequest);
                }
            }
        }

        // 更新操作
        if (rims == null) return;
        for (IndexSourceModel rim : rims) {
            IndexRequest indexRequest = new IndexRequest(getIndex(serviceIndex), serviceIndex.type(), rim.getKey())
                .source(JsonUtil.toJson(rim), XContentType.JSON);
            boolean isupdate = updateIndex(indexRequest);
            if (!isupdate) {
                // 重试一次
                serviceIndexNameFactory.restart();
                updateIndex(indexRequest);
            }
        }
    }

    private boolean noExists(List<IndexSourceModel> rims, String id) {
        if (TypeUtil.isNull(rims)) return true;
        for (IndexSourceModel index : rims) {
            if (id.equals(index.getId())) {
                return false;
            }
        }
        return true;
    }

    private boolean updateIndex(IndexRequest indexRequest) {
        RestHighLevelClient client = serviceIndexNameFactory.getClient();
        try {
            IndexResponse indexResponse = client.index(indexRequest, ServiceIndexNameFactory.COMMON_OPTIONS);
            if (indexResponse == null) return false;
            if (indexResponse.getVersion() > 0) return true;
            return false;
        } catch (IOException e) {
            log.error("更新ES失败", e);
            return false;
        }
    }

    private boolean deleteIndex(DeleteRequest deleteRequest) {
        RestHighLevelClient client = serviceIndexNameFactory.getClient();
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, ServiceIndexNameFactory.COMMON_OPTIONS);
            if (deleteResponse == null) return false;
            if (deleteResponse.getVersion() > 0) return true;
            return false;
        } catch (IOException e) {
            log.error("更新ES失败", e);
            return false;
        }
    }

    private String getIndex(ServiceIndex serviceIndex) {
        return INDEX_DEV + serviceIndex.index();
    }

}
