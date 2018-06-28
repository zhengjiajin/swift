/*
 * @(#)AbstractBeanDataModel.java   1.0  2016年4月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 以Bean类的方式实现 DataModel 所有传输接口的Bean类都必须实现此类。
 * 
 * <pre>
 * <strong>Bean类实现的约定：</strong>
 * <li>Bean类必须包含一个默认构造函数（即没有参数的构造函数）</li>
 * <li>Bean类的属性类型不能为接口（List和Map除外）</li>
 * <li>Bean类的属性设置方法必须使用set,get,is三个方式</li>
 * </pre>
 * 
 * @author zhengjiajin
 * @version 1.0 2016年4月26日
 */
public abstract class AbstractBeanDataModel implements DataModel {

    private static final Set<Class<?>> SUPPORTED_CLASS = new HashSet<Class<?>>() {
		private static final long serialVersionUID = 1L;

		{
			add(boolean.class);
			add(byte.class);
			add(short.class);
			add(int.class);
			add(long.class);
			add(float.class);
			add(double.class);

			add(Boolean.class);
			add(Byte.class);
			add(Short.class);
			add(Integer.class);
			add(Long.class);
			add(Float.class);
			add(Double.class);

			add(String.class);

			add(Date.class);
		}
	};

	private Set<String> properties = new HashSet<String>();

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#addObject(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void addObject(String objectPath, Object value) {
		addObject(objectPath, 0, value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#addObject(java.lang.String,
	 *      int, java.lang.Object)
	 */
	@Override
	public void addObject(String objectPath, int index, Object value) {
		addObject(objectPath, value, index, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#putObject(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void putObject(String path, Object value) {
		putObject(path, value, this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataModel getDataModel(String objectPath) throws RuntimeException {
		Object value = getObjectInternal(objectPath, this);
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			value = ((List) value).get(0);
		}
		if (value instanceof DataModel) {
			return (DataModel) value;
		}
		throw new ClassCastException(value.getClass().getName() + " connot cast to " + DataModel.class.getName());
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getObject(java.lang.String,
	 *      java.lang.Class)
	 */
	@Override
	public Object getObject(String objectPath) {
		return getObjectInternal(objectPath, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getObject(java.lang.String,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataModel> T getObject(String objectPath, Class<T> classType) {
		return (T) getObjectInternal(objectPath, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getList(java.lang.String)
	 */
	@Override
	public List<DataModel> getList(String objectPath) {
		return getListInternal(objectPath, DataModel.class);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getList(java.lang.String,
	 *      java.lang.Class)
	 */
	@Override
	public <T extends DataModel> List<T> getList(String objectPath, Class<T> classType) {
		return getListInternal(objectPath, classType);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getString(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getString(String objectPath) {
		Object value = getObject(objectPath);
		if (value instanceof List) {
			return TypeUtil.toString(((List) value).get(0));
		}
		return TypeUtil.toString(value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getLong(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Long getLong(String objectPath) {
		Object value = getObjectInternal(objectPath, this);
		if (value instanceof List) {
			Object o = ((List) value).get(0);
			if (o == null) {
				return null;
			}
			return TypeUtil.toLong(o);
		}
		return TypeUtil.toLong(value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getInteger(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getInteger(String objectPath) {
		Object value = getObjectInternal(objectPath, this);
		if (value instanceof List) {
			Object o = ((List) value).get(0);
			if (o == null) {
				return null;
			}
			return TypeUtil.toInt(o);
		}
		return TypeUtil.toInt(value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getFloat(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Float getFloat(String objectPath) {
		Object value = getObjectInternal(objectPath, this);
		if (value instanceof List) {
			Object o = ((List) value).get(0);
			if (o == null) {
				return null;
			}
			return TypeUtil.toFloat(o);
		}
		return TypeUtil.toFloat(value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getDouble(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Double getDouble(String objectPath) {
		Object value = getObjectInternal(objectPath, this);
		if (value instanceof List) {
			Object o = ((List) value).get(0);
			if (o == null) {
				return null;
			}
			return TypeUtil.toDouble(o);
		}
		return TypeUtil.toDouble(value);
	}
	/** 
     * @see com.hhmk.hospital.common.model.service.data.DataModel#getListObject(java.lang.String)
     */
    @Override
    public List<Object> getListObject(String objectPath) {
        return getListInternal(objectPath, Object.class);
    }
	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListString(java.lang.String)
	 */
	@Override
	public List<String> getListString(String objectPath) {
		return getListInternal(objectPath, String.class);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListInteger(java.lang.String)
	 */
	@Override
	public List<Integer> getListInteger(String objectPath) {
		return getListInternal(objectPath, Integer.class);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListDouble(java.lang.String)
	 */
	@Override
	public List<Double> getListDouble(String objectPath) {
		return getListInternal(objectPath, Double.class);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListFloat(java.lang.String)
	 */
	@Override
	public List<Float> getListFloat(String objectPath) {
		return getListInternal(objectPath, Float.class);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#remove(java.lang.String)
	 */
	@Override
	public void remove(String objectPath) {
		remove(objectPath, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getBoolean(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Boolean getBoolean(String objectPath) throws RuntimeException {
		Object value = getObjectInternal(objectPath, this);
		if (value instanceof List) {
			Object o = ((List) value).get(0);
			if (o == null) {
				return null;
			}
			return TypeUtil.toBoolen(o);
		}
		return TypeUtil.toBoolen(value);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListBoolean(java.lang.String)
	 */
	@Override
	public List<Boolean> getListBoolean(String objectPath) throws RuntimeException {
		return getListInternal(objectPath, Boolean.class);
	}

	@Override
	public Set<String> keySet() {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(getClass(), Object.class);
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			Set<String> keys = new HashSet<String>();
			if (descriptors == null) {
				return keys;
			}

			for (PropertyDescriptor descriptor : descriptors) {
				keys.add(descriptor.getName());
			}
			return keys;
		} catch (IntrospectionException ex) {
			throw new RuntimeException("An exception occurs during reflective operation", ex);
		}
	}

	public static <T extends DataModel> T mapToBean(DataModel beanData,Class<T> cla) {
		return JsonUtil.toObj(JsonUtil.toJson(beanData), cla);
	}

	protected void addObject(String path, Object value, int index, Object parent) {
		if (path == null) {
			throw new RuntimeException("path must not be null");
		}
		if (value == null) {
			return;
		}
		validateDataType(value);

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			finalAddObject(path, value, index, parent);
			return;
		}

		String name = path.substring(0, idx);
		String normalizeName = DataModelUtil.normalize(name);
		path = path.substring(idx + 1);
		PropertyDescriptor descriptor = getPropertyDescriptor(name, parent.getClass());
		Class<?> propertyClass = getPropertyClass(descriptor);
		if (propertyClass == List.class) {
			addObjectToList(path, value, parent, descriptor);
		} else if (propertyClass == Map.class) {
			Object instance = getPropertyValue(descriptor, parent);
			if (instance == null) {
				instance = new MapDataModel();
			}
			if (instance instanceof MapDataModel) {
				((MapDataModel) instance).addObject(path, value);
			} else {
				addObject(path, value, 0, instance);
			}
			addObject(normalizeName, instance, 0, parent);
		} else {
			try {
				Object instance = getPropertyValue(descriptor, parent);
				if (instance == null) {
					instance = propertyClass.newInstance();
					addObject(normalizeName, instance, 0, parent);
				}
				addObject(path, value, index, instance);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException("An exception occurs during instance object: " + propertyClass.getName(), ex);
			}
		}
	}

	protected void putObject(String path, Object value, Object parent) {
		if (path == null || EMPTY.equals(path)) {
			throw new RuntimeException("path must not be null or empty");
		}
		if (value == null) {
			return;
		}

		int index = path.indexOf(DOT);
		if (index == -1) {
			finalAddObject(path, value, 0, parent);
			return;
		}

		String name = path.substring(0, index);
		String normalizeName = DataModelUtil.normalize(name);
		path = path.substring(index + 1);
		PropertyDescriptor descriptor = getPropertyDescriptor(name, parent.getClass());
		Class<?> propertyClass = getPropertyClass(descriptor);
		if (propertyClass == List.class) {
			processObject(path, value, parent, descriptor, false);
		} else if (propertyClass == Map.class) {
			Object instance = getPropertyValue(descriptor, parent);
			if (instance == null) {
				instance = new MapDataModel();
			}
			if (instance instanceof MapDataModel) {
				((MapDataModel) instance).putObject(path, value);
			} else {
				putObject(path, value, instance);
			}
			putObject(normalizeName, instance, parent);
		} else {
			try {
				Object instance = getPropertyValue(descriptor, parent);
				if (instance == null) {
					instance = propertyClass.newInstance();
					putObject(normalizeName, instance, parent);
				}
				putObject(path, value, instance);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException("An exception occurs during instance object: " + propertyClass.getName(), ex);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> T getObjectInternal(String path, Class<T> clazz, Object parent) {
		Object value = getObjectInternal(path, parent);
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			value = ((List) value).get(0);
		}
		if (clazz.isAssignableFrom(value.getClass())) {
			return (T) value;
		}
		throw new ClassCastException(value.getClass().getName() + " connot cast to " + clazz.getName());
	}

	@SuppressWarnings("rawtypes")
	protected Object getObjectInternal(String path, Object parent) {
		if (path == null || EMPTY.equals(path)) {
			throw new RuntimeException("path must not be null");
		}

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			return getPropertyValue(path, parent);
		}

		String name = path.substring(0, idx);
		String subPath = path.substring(idx + 1);
		Object o = getPropertyValue(name, parent);
		if (o == null) {
			return null;
		}
		if (o instanceof List) {
			o = getObjectFromList(name, (List) o);
		}
		return getObjectInternal(subPath, o);
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> getListInternal(String path, Class<T> clazz) {
		Object value = getObjectInternal(path, this);
		if (value == null) {
			return new ArrayList<T>();
		} else if (value instanceof List) {
			return (List<T>) value;
		}
		return Arrays.asList((T) value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void remove(String path, Object parent) {
		if (parent == null) {
			return;
		}
		int idx = path.indexOf(DOT);
		if (idx == -1) {
			if (parent instanceof List) {
				List<Object> list = (List<Object>) parent;
				for (Object item : list) {
					remove(path, item);
				}
				return;
			} else if (parent instanceof Map) {
				((Map) parent).remove(DataModelUtil.normalize(path));
				return;
			}
			int i = DataModelUtil.getIndex(path);
			PropertyDescriptor descriptor = getPropertyDescriptor(path, parent.getClass());
			if (i != -1 && getPropertyClass(descriptor) == List.class) {
				removeFromList((List<Object>) getPropertyValue(descriptor, parent), i);
				return;
			}
			setPropertyValue(path, null, parent);
			return;
		}

		String name = path.substring(0, idx);
		path = path.substring(idx + 1);
		Object value = getPropertyValue(name, parent);
		if (value == null) {
			return;
		}
		if (value instanceof List) {
			List<Object> list = (List<Object>) value;
			int i = DataModelUtil.getIndex(name);
			if (i != -1) {
				i = Math.max(i, 0);
				i = Math.min(i, list.size() - 1);
				remove(path, list.get(i));
			}
		} else {
			remove(path, value);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void finalAddObject(String key, Object value, int index, Object parent) {
		PropertyDescriptor descriptor = getPropertyDescriptor(key, parent.getClass());
		Class<?> propertyCalss = getPropertyClass(descriptor);
		if (propertyCalss == null) {
			throw new RuntimeException("Property '" + DataModelUtil.normalize(key) + "' type is null");
		} else if (propertyCalss.isAssignableFrom(List.class) && !(value instanceof List)) {
			List<Object> list = (List<Object>) getPropertyValue(descriptor, parent);
			if (list == null) {
				list = new ArrayList<Object>();
			}
			if (index <= 0) {
				list.add(0, value);
			} else if (index >= list.size()) {
				list.add(value);
			}
			value = list;
		}
		setPropertyValue(descriptor, value, parent);
	}

	private void addObjectToList(String path, Object value, Object parent, PropertyDescriptor descriptor) {
		processObject(path, value, parent, descriptor, true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processObject(String path, Object value, Object parent, PropertyDescriptor descriptor, boolean isAdd) {
		List propertyValue = (List) getPropertyValue(descriptor, parent);
		if (propertyValue == null) {
			propertyValue = new ArrayList();
			Object element = createElementInstance(path, value, descriptor);
			propertyValue.add(element);
			if (isAdd) {
				addObject(descriptor.getName(), propertyValue, 0, parent);
			} else {
				putObject(descriptor.getName(), propertyValue, parent);
			}
			return;
		}

		Object item = propertyValue.get(propertyValue.size() - 1);
		if (!(item instanceof DataModel)) {
			throw new RuntimeException("Property type '" + item.getClass() + "' is not an instance of "
					+ DataModel.class);
		}
		boolean needCreate = false;
		String nextToken = DataModelUtil.nextToken(path);
		if (item instanceof AbstractBeanDataModel && ((AbstractBeanDataModel) item).properties.contains(nextToken)) {
			needCreate = true;
		} else if (item instanceof MapDataModel && ((MapDataModel) item).containsKey(nextToken)) {
			needCreate = true;
		}
		if (needCreate) {
			Object element = createElementInstance(path, value, descriptor);
			propertyValue.add(element);
		} else {
			if (isAdd) {
				((DataModel) item).addObject(path, value);
			} else {
				((DataModel) item).putObject(path, value);
			}
		}
	}

	private Object createElementInstance(String path, Object value, PropertyDescriptor descriptor) {
		Type[] parameterTypes = getParameterizedTypes(descriptor);
		if (parameterTypes == null) {
			throw new RuntimeException("No parameterized type provide in property: " + descriptor.getName());
		}
		Class<?> clazz = (Class<?>) parameterTypes[0];
		Constructor<?> constructor = getDefaultConstructor(clazz);
		try {
			Object element = constructor.newInstance();
			if (element instanceof DataModel) {
				((DataModel) element).addObject(path, value);
				if (element instanceof AbstractBeanDataModel) {
					((AbstractBeanDataModel) element).properties.add(DataModelUtil.nextToken(path));
				}
			} else {
				addObject(path, value, 0, element);
			}
			return element;
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Failed to create an instance of a class: " + clazz);
		}
	}

	private void removeFromList(List<Object> list, int index) {
		index = Math.max(index, 0);
		index = Math.min(index, list.size() - 1);
		list.remove(index);
	}

	@SuppressWarnings("rawtypes")
	private Object getPropertyValue(String propertyName, Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof List) {
			return getObjectFromList(propertyName, (List) obj);
		}
		if (obj instanceof Map) {
			return ((Map) obj).get(DataModelUtil.normalize(propertyName));
		}

		Method method = null;
		try {
			method = getReadMethod(propertyName, obj);
		} catch (Throwable ex) {
			return null;
		}
		try {
			return method.invoke(obj);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("An exception occurs during reflective operation", ex);
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getObjectFromList(String propertyName, List list) {
		int i = DataModelUtil.getIndex(DataModelUtil.stripAsterisk(propertyName));
		if (i == -1) {
			return list;
		}
		i = Math.max(i, 0);
		i = Math.min(i, list.size());
		return list.get(i);
	}

	private Object getPropertyValue(PropertyDescriptor descriptor, Object obj) {
		try {
			return getReadMethod(descriptor).invoke(obj);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("An exception occurs during reflective operation", ex);
		}
	}

	private void setPropertyValue(String propertyName, Object value, Object parent) {
		try {
			getWriteMethod(propertyName, parent).invoke(parent, value);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("An exception occurs during reflective operation", ex);
		}
	}

	private void setPropertyValue(PropertyDescriptor descriptor, Object value, Object parent) {
		try {
			getWriteMethod(descriptor).invoke(parent, value);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("An exception occurs during reflective operation", ex);
		}
	}

	private PropertyDescriptor getPropertyDescriptor(String propertyName, Class<?> beanClass) {
		try {
			propertyName = DataModelUtil.normalize(propertyName);
			return new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException ex) {
			throw new RuntimeException("An exception occurs during introspection: " + propertyName, ex);
		}
	}

	private Method getWriteMethod(PropertyDescriptor descriptor) {
		Method method = descriptor.getWriteMethod();
		if (method == null) {
			throw new RuntimeException("No setter for property: " + descriptor.getName());
		}
		return method;
	}

	private Method getWriteMethod(String propertyName, Object obj) {
		PropertyDescriptor descriptor = getPropertyDescriptor(propertyName, obj.getClass());
		return getWriteMethod(descriptor);
	}

	private Method getReadMethod(PropertyDescriptor descriptor) {
		Method method = descriptor.getReadMethod();
		if (method == null) {
			throw new RuntimeException("No getter for property: " + descriptor.getName());
		}
		return method;
	}

	private Method getReadMethod(String propertyName, Object obj) {
		PropertyDescriptor descriptor = getPropertyDescriptor(propertyName, obj.getClass());
		return getReadMethod(descriptor);
	}

	private Class<?> getPropertyClass(PropertyDescriptor descriptor) {
		return descriptor.getPropertyType();
	}

	private Constructor<?> getDefaultConstructor(Class<?> clazz) {
		Class<?>[] empty = {};
		try {
			return clazz.getDeclaredConstructor(empty);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("No default constructor: " + clazz.getName());
		} catch (SecurityException ex) {
			throw new RuntimeException("An exception occurs during get default constructor: " + clazz.getName());
		}
	}

	private Type[] getParameterizedTypes(PropertyDescriptor descriptor) {
		Type genericType = getReadMethod(descriptor).getGenericReturnType();
		if (!ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
			return null;
		}
		return ((ParameterizedType) genericType).getActualTypeArguments();
	}

	private void validateDataType(Object value) {
		if (!SUPPORTED_CLASS.contains(value.getClass()) && !(value instanceof List) && !(value instanceof Map)
				&& !(value instanceof DataModel)) {
			throw new RuntimeException("Unsupported data type: " + value.getClass());
		}
	}
}
