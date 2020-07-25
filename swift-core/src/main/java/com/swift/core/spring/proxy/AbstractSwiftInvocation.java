/*
 * @(#)AbstractSwiftInvocation.java   1.0   2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.parser.DataJsonParser;
import com.swift.exception.extend.SystemException;
import com.swift.util.bean.BeanUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 动态代理处理类必须继承此类 
 * @author zhengjiajin
 * @version 1.0  2020年4月15日
 */
public abstract class AbstractSwiftInvocation implements InvocationHandler {

    /** 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
        }
        return invokeInterface(proxy, method, args);
    }
    //得到参数的最终实现接口
    protected Class<?> getInvokeClass(Object[] args){
        if(args==null || args.length<1) throw new SystemException("输入参数数量不正错");
        if(!(args[0] instanceof Class)) throw new SystemException("输入参数格式不正错");
        Class<?> cla = (Class<?>)args[0];
        return cla;
    }
    //把VALUE值转为最终的对象类型
    protected Object valueToType(Object value,Type type) {
        if(TypeUtil.isNull(value))return null;
        String json = JsonUtil.toJson(value);
        if(type instanceof ParameterizedType) {
            ParameterizedType paraType = (ParameterizedType)type;
            //map则直接处理
            if(Map.class.getTypeName().equals(paraType.getRawType().getTypeName())) {
                Class<?> typeCla = BeanUtil.classForName(paraType.getRawType().getTypeName());
                return JsonUtil.toObj(json, typeCla);
            }
            //list需要转换成对应对象
            if(List.class.getTypeName().equals(paraType.getRawType().getTypeName())) {
                if(paraType.getActualTypeArguments()!=null && paraType.getActualTypeArguments().length>0) {
                    Type listValueType = paraType.getActualTypeArguments()[0];
                    Class<?> typeCla = BeanUtil.classForName(listValueType.getTypeName());
                    return JsonUtil.toListObject(json, typeCla);
                }else {
                    return JsonUtil.toObj(json, List.class);
                }
                
            }
            throw new SystemException("不支持的数据输出格式");
            
        }else {
            if(DataModel.class.getTypeName().equals(type.getTypeName())) {
                return DataJsonParser.jsonToObject(json); 
            } else {
                Class<?> typeCla = BeanUtil.classForName(type.getTypeName());
                return JsonUtil.toObj(json, typeCla); 
            }
        }
    }
    
    //得到对象注解里的返回参数类型
    protected Type getResponseType(Class<?> cla) {
        if(cla.getGenericInterfaces()==null) return null;
        for (Type type : cla.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                Type[] typePara = ((ParameterizedType) type).getActualTypeArguments();
                if(typePara==null || typePara.length!=2) continue;
                return typePara[1];
            }
        }
        for (Type type : cla.getGenericInterfaces()) {
            Type superType = getResponseType(BeanUtil.classForName(type.getTypeName()));
            if(superType!=null) return superType;
        }
        return null;
    }
    
    public abstract Object invokeInterface(Object proxy, Method method, Object[] args) throws Throwable;
}
