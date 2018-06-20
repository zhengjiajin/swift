/*
 * @(#)ParamPastDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import java.util.Date;

import com.swift.core.validator.annotation.ParamPast;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.date.DateUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamPastDef extends ConstraintDef<ParamPast> {
    private static ParamPastDef def = new ParamPastDef();

    private ParamPastDef() {
    }

    public static ParamPastDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamPast anno) {
        if(obj==null) return true;
        Date date = DateUtil.toDate(obj);
        return DateUtil.beforeNow(date);
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamPast anno) {
        return getLocalMessage(anno.anno().message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamPast anno) {
        return anno.param();
    }

}
