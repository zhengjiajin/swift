/*
 * @(#)ParamFutureDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import java.util.Date;

import com.swift.core.validator.annotation.ParamFuture;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.date.DateUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamFutureDef extends ConstraintDef<ParamFuture> {
    private static ParamFutureDef def = new ParamFutureDef();

    private ParamFutureDef() {
    }

    public static ParamFutureDef getDef() {

        return def;
    }


    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamFuture anno) {
        if(obj==null) return true;
        Date date = DateUtil.toDate(obj);
        return DateUtil.afterNow(date);
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamFuture anno) {
        return getLocalMessage(anno.anno().message());
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamFuture anno) {
        return anno.param();
    }

}
