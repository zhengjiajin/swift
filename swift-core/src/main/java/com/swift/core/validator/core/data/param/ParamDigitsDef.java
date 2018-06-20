/*
 * @(#)ParamDigitsDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data.param;

import com.swift.core.validator.annotation.ParamDigits;
import com.swift.core.validator.core.data.ConstraintDef;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public class ParamDigitsDef extends ConstraintDef<ParamDigits> {
    private static ParamDigitsDef def = new ParamDigitsDef();

    private ParamDigitsDef() {
    }

    public static ParamDigitsDef getDef() {

        return def;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#checkObj(java.lang.Object, java.lang.annotation.Annotation)
     */
    @Override
    protected boolean checkObj(Object obj, ParamDigits anno) {
        if(obj==null) return true;
        if(!TypeUtil.isNumber(obj)) return false;
        String str = TypeUtil.toString(obj);
        String inter = "";
        String fraction = "";
        if(str.indexOf(".")!=-1) {
            inter=str.substring(0,str.indexOf("."));
            fraction=str.substring(str.indexOf(".")+1);
        }else {
            inter=str;
        }
        if(inter.length()>anno.anno().integer()) return false;
        if(fraction.length()>anno.anno().fraction()) return false;
        return true;
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#formatMsg(java.lang.annotation.Annotation)
     */
    @Override
    protected String formatMsg(ParamDigits anno) {
        String msg = getLocalMessage(anno.anno().message());
        return msg.replace("{integer}", TypeUtil.toString(anno.anno().integer())).replace("{fraction}", TypeUtil.toString(anno.anno().fraction()));
    }

    /** 
     * @see com.swift.core.validator.core.data.ConstraintDef#getParam(java.lang.annotation.Annotation)
     */
    @Override
    protected String getParam(ParamDigits anno) {
        return anno.param();
    }


}
