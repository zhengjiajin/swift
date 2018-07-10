/*
 * @(#)UpdateServiceIndexFilter.java   1.0  2016年11月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.index.es.filter;

import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.swift.core.filter.ResponseFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.ReqInterfaceFactory;
import com.swift.core.spring.Spring;
import com.swift.dao.index.es.annontation.ServiceIndex;
import com.swift.dao.index.es.annontation.ServiceIndex.Tnteraction;
import com.swift.dao.index.es.factory.ServiceIndexNameFactory;
import com.swift.dao.index.es.factory.UpdateIndexService;
import com.swift.dao.index.es.factory.UpdateIndexService.ReturnIndexModel;
import com.swift.exception.SwiftRuntimeException;
import com.swift.util.bean.AnnotationUtil;

/**
 * 添加说明
 *
 * @author zhengjiajin
 * @version 1.0 2016年11月28日
 */
@Service
@Order(1)
public class UpdateServiceIndexFilter implements ResponseFilter {

    @Autowired
    private ServiceIndexNameFactory serviceIndexNameFactory;

    /**
     * @see com.hhmk.hospital.common.filter.ResponseFilter#doFilter(com.ServiceResponse.hospital.common.model.ServerResponse)
     */
    @Override
    public void doFilter(ServiceResponse res)  {
        if (res.getResultCode() != 0) return;
        ServiceRequest req = res.getRequest();
        Object obj = ReqInterfaceFactory.getInterfaceTrueObj(req.getMethod(), req.getInterfaceVersion());
        ServiceIndex serviceIndex = AnnotationUtil.getAnnotation(obj.getClass(), ServiceIndex.class);
        if (serviceIndex == null) return;
        UpdateIndexService updateIndexService = Spring.getBean(serviceIndex.updateIndexClassName());
        if(updateIndexService==null) return;
        List<ReturnIndexModel> rims = updateIndexService.updateKey(getIds(serviceIndex, res));
        updateIndex(serviceIndex, rims);
    }

    private String[] getIds(ServiceIndex serviceIndex, ServiceResponse res)  {
        DataModel dm = null;
        if (serviceIndex.interaction().equals(Tnteraction.request)) {
            dm = res.getRequest().getData();
        } else if (serviceIndex.interaction().equals(Tnteraction.response)) {
            dm = res.getData();
        }
        List<String> ids = dm.getListString(serviceIndex.primaryPath());
        if (ids == null || ids.isEmpty()) throw new SwiftRuntimeException("取不到更新索引标志ID值");
        String[] sss = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            sss[i] = ids.get(i);
        }
        return sss;
    }

    private void updateIndex(ServiceIndex serviceIndex, List<ReturnIndexModel> rims) {
        if (rims == null) return;
        BulkRequestBuilder bb = serviceIndexNameFactory.getClient().prepareBulk();
        for (ReturnIndexModel rim : rims) {
            IndexRequest up = new IndexRequest(serviceIndex.IndexName(),serviceIndex.typeKey(), rim.getKey());
            up.source(rim.getDataModel().toString());
            bb.add(up);
        }
        bb.execute();
    }

}
