/*
 * @(#)ConstraintDef.java   1.0  2018年6月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core.data;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.swift.core.model.data.DataModel;
import com.swift.core.validator.core.ValidatorBuilderString;
import com.swift.util.type.TypeUtil;

/**
 * 只能为false 只能为true 必须小于或等于{value} 必须大于或等于{value} 数字的值超出了允许范围(只允许在{integer}位整数和{fraction}位小数范围内) 需要是一个将来的时间
 * 最大不能超过{value} 最小不能小于{value} 不能为null 必须为null 需要是一个过去的时间 需要匹配正则表达式"{regexp}" 个数必须在{min}和{max}之间 不合法的信用卡号码 不是一个合法的电子邮件地址
 * 长度需要在{min}和{max}之间 不能为空 不能为空 需要在{min}和{max}之间 可能有不安全的HTML内容 执行脚本表达式"{script}"没有能够得到true 需要是一个合法的URL
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月20日
 */
public abstract class ConstraintDef<ANNO extends Annotation> {

    private static ResourceBundle rb = ResourceBundle.getBundle("org/hibernate/validator/ValidationMessages",
        Locale.CHINA);

    public String valueDef(DataModel data, ANNO anno) {
        List<DataModel> list = getLastDataModel(data, getParam(anno));
        String param = getLastParam(getParam(anno));
        if (TypeUtil.isNull(list)) return null;
        List<String> listStr = new LinkedList<String>();
        for (DataModel model : list) {
            Object obj = model.getObject(param);
            boolean check = checkObj(obj, anno);
            if (!check) listStr.add(ValidatorBuilderString.builderString(param, formatMsg(anno)));
        }
        return ValidatorBuilderString.builderString(listStr);
    }

    protected abstract boolean checkObj(Object obj, ANNO anno);

    protected abstract String formatMsg(ANNO anno);

    protected abstract String getParam(ANNO anno);

    protected List<DataModel> getLastDataModel(DataModel data, String param) {
        if (data == null) return null;
        if (param.indexOf(".") == -1) return Arrays.asList(data);
        return data.getList(getFirstParam(param));
    }

    protected String getFirstParam(String param) {
        if (TypeUtil.isNull(param)) return param;
        if (param.indexOf(".") != -1) return param.substring(0, param.lastIndexOf("."));
        return param;
    }

    protected String getLastParam(String param) {
        if (TypeUtil.isNull(param)) return param;
        if (param.indexOf(".") != -1) return param.substring(param.lastIndexOf(".") + 1);
        return param;
    }

    protected String getLocalMessage(String msg) {
        if (TypeUtil.isNull(msg)) return msg;
        if (msg.indexOf(".constraints.") != -1) {
            return rb.getString(msg.replace("{", "").replace("}", ""));
        }
        return msg;
    }
}
