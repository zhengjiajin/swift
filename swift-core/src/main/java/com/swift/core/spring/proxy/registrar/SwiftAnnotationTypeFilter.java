/*
 * @(#)SwiftAnnotationTypeFilter.java   1.0  2020年4月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy.registrar;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.BeanUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2020年4月20日
 */
public class SwiftAnnotationTypeFilter extends AnnotationTypeFilter {

    public SwiftAnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        super(annotationType, true, false);
    }

    public SwiftAnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
        super(annotationType, considerMetaAnnotations, false);
    }

    @Override
    protected boolean matchSelf(MetadataReader metadataReader) {
        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
        Class<?> cla = BeanUtil.classForName(metadata.getClassName());
        // 非接口直接返回FALSE
        if (metadata.hasAnnotation(super.getAnnotationType().getName())) {
            return checkEndInteface(cla);
        }
        if (metadata.hasMetaAnnotation(super.getAnnotationType().getName())) return true;
        Object obj = AnnotationUtil.getAnnotation(cla, super.getAnnotationType());
        if (obj != null) {
            return checkEndInteface(cla);
        }
        return false;
    }
    
    private boolean checkEndInteface(Class<?> cla) {
        for (Type type : cla.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                for (Type typePara : ((ParameterizedType) type).getActualTypeArguments()) {
                    if(typePara.getTypeName().length()<=5) return false;//去掉未定义的泛型
                }
            }
        }
        return true;
    }
}
