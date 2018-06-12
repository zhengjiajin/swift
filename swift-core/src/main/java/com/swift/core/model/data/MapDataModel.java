/*
 * @(#)MapDataModel.java   1.0  2016年4月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.swift.exception.SwiftRuntimeException;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 以Map+List的方式实现DataModel
 * 
 * @author zhengjiajin
 * @version 1.0 2016年4月26日
 */
public class MapDataModel extends LinkedHashMap<String, Object> implements DataModel {

	private static final long serialVersionUID = 1042686040983756802L;

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
		addObject(objectPath, index, value, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#putObject(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void putObject(String objectPath, Object value) {
		putObject(objectPath, value, this);
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getObject(java.lang.String)
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
		Object value = this.getObject(objectPath);
		if (value instanceof List) {
			return TypeUtil.toString(((List) value).get(0));
		}
		if (value instanceof Map) {
			try {
				return JsonUtil.toJson(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	@SuppressWarnings("rawtypes")
	public static MapDataModel beanToMap(AbstractBeanDataModel beanData) {
		if (beanData == null) {
			return null;
		}
		MapDataModel m = new MapDataModel();
		try {
    		BeanInfo beanInfo = Introspector.getBeanInfo(beanData.getClass(), Object.class);
    		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    		if (descriptors == null) {
    			return m;
    		}
    		for (PropertyDescriptor descriptor : descriptors) {
    			Method method = descriptor.getReadMethod();
    			Object value = method.invoke(beanData);
    			if (value == null) {
    				continue;
    			}
    			if (value instanceof List) {
    				List values = (List) value;
    				List<Object> maps = new ArrayList<Object>(values.size());
    				for (Object o : values) {
    					if (o instanceof AbstractBeanDataModel) {
    						maps.add(beanToMap((AbstractBeanDataModel) o));
    					} else {
    						maps.add(o);
    					}
    				}
    				m.addObject(descriptor.getName(), maps);
    			} else if (value instanceof AbstractBeanDataModel) {
    				m.addObject(descriptor.getName(), beanToMap((AbstractBeanDataModel) value));
    			} else {
    				m.addObject(descriptor.getName(), value);
    			}
    		}
		}catch (Exception e) {
           throw new SwiftRuntimeException("change obj error",e);
        }
		return m;
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getDataModel(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		if (value instanceof Map) {
		    MapDataModel model = new MapDataModel();
		    model.putAll((Map)value);
            return model;
        }
		throw new ClassCastException(value.getClass().getName() + " connot cast to " + DataModel.class.getName());
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
     * @see com.hhmk.hospital.common.model.service.data.DataModel#getListObject(java.lang.String)
     */
    @Override
    public List<Object> getListObject(String objectPath) throws RuntimeException {
        return getListInternal(objectPath, Object.class);
    }
	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getListBoolean(java.lang.String)
	 */
	@Override
	public List<Boolean> getListBoolean(String objectPath) throws RuntimeException {
		return getListInternal(objectPath, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	protected void addObject(String path, int index, Object value, Map<String, Object> parent) {
		if (path == null) {
			throw new RuntimeException("path must not be null");
		}
		if (value == null) {
			return;
		}

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			finalAddObject(path, value, index, parent);
			return;
		}

		String name = path.substring(0, idx);
		path = path.substring(idx + 1);
		Object o = parent.get(DataModelUtil.normalize(name));
		if (o == null) {
			MapDataModel m = new MapDataModel();
			m.addObject(path, index, value);
			addObject(name, m);
			return;
		}
		if (o instanceof List) {
			List<MapDataModel> list = (List<MapDataModel>) o;
			if (list.size() > 0) {
				MapDataModel m = list.get(list.size() - 1);
				String token = DataModelUtil.nextToken(path);
				if (m.containsKey(token)) {
					MapDataModel m1 = new MapDataModel();
					m1.addObject(path, value);
					list.add(m1);
				} else {
					m.addObject(path, value);
				}
			}
			return;
		}
		if (!(o instanceof DataModel)) {
			throw new RuntimeException("Cannot add property '" + DataModelUtil.nextToken(path) + "' to object: "
					+ o.getClass().getName());
		}
		((DataModel) o).addObject(path, index, value);
	}

	protected void putObject(String path, Object value, Map<String, Object> parent) {
		if (path == null) {
			throw new RuntimeException("path must not be null");
		}
		if (value == null) {
			return;
		}

		int index = path.indexOf(DOT);
		if (index == -1) {
			if (path.startsWith(ASTERISK)) {
				addObject(path, value);
			} else {
				parent.put(DataModelUtil.normalize(path), value);
			}
			return;
		}

		String name = path.substring(0, index);
		String subPath = path.substring(index + 1);
		Object o = parent.get(DataModelUtil.normalize(name));
		if (o == null) {
			MapDataModel m = new MapDataModel();
			m.putObject(subPath, value);
			o = m;
			if (name.startsWith(ASTERISK)) {
				List<MapDataModel> list = new ArrayList<MapDataModel>();
				list.add(m);
				o = list;
			}
			putObject(name, o, parent);
			return;
		}

		if (o instanceof List) {
			addObject(path, value);
			return;
		}
		MapDataModel m1 = (MapDataModel) o;
		m1.putObject(subPath, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getObjectInternal(String path, Map<String, Object> parent) {
		if (path == null || EMPTY.equals(path)) {
			throw new RuntimeException("path must not be null");
		}

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			Object v = parent.get(DataModelUtil.normalize(path));
			if (v == null) {
				return null;
			}

			if (v instanceof List) {
				int i = DataModelUtil.getIndex(path);
				if (i > -1) {
					return ((List) v).get(i);
				}
			}
			return v;
		}

		String name = path.substring(0, idx);
		String subPath = path.substring(idx + 1);
		Object o = parent.get(DataModelUtil.normalize(name));
		if (o == null) {
			return null;
		}
		if (o instanceof List) {
			int i = DataModelUtil.getIndex(name);
			o = ((List) o).get(i < 0 ? 0 : i);
		}
		if (o instanceof Map) {
			return getObjectInternal(subPath, (Map<String, Object>) o);
		}
		throw new RuntimeException("Child type " + o.getClass().getName() + " is not a instance of java.util.Map");
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
	protected void remove(String path, Map<String, Object> parent) {
		if (path == null) {
			return;
		}

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			Object o = parent.get(DataModelUtil.normalize(path));
			if (o == null) {
				return;
			}
			if (o instanceof List) {
				List list = (List) o;
				int i = DataModelUtil.getIndex(path);
				if (i > -1 && i < list.size()) {
					list.remove(i);
				}
				return;
			}
			parent.remove(DataModelUtil.normalize(path));
			return;
		}
		String name = path.substring(0, idx);
		Object value = parent.get(name);
		if (value != null && value instanceof Map) {
			remove(path.substring(idx + 1), (Map<String, Object>) value);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void finalAddObject(String key, Object value, int index, Map<String, Object> parent) {
		boolean isArray = false;
		if (key.startsWith(ASTERISK)) {
			key = DataModelUtil.stripAsterisk(key);
			isArray = true;
		}
		Object old = parent.get(key);
		if (old == null) {
			if (!isArray) {
				parent.put(key, value);
				return;
			}
		}

		List<Object> list = null;
		if (old instanceof List) {
			list = (List) old;
		} else {
			list = new ArrayList<Object>();
			if (old != null) {
				list.add(old);
			}
		}
		if (index > 0) {
			list.add(index, value);
		} else {
			list.add(value);
		}
		parent.put(key, list);
	}

	public static void main(String[] args) throws IOException {
		MapDataModel s1 = new MapDataModel();
		s1.addObject("a", 1);
		System.out.println(s1);
		// s1.remove("a");
		System.out.println(s1);
		s1.addObject("*abc.a", 2);
		System.out.println(s1);
		// s1.remove("abc.a");
		System.out.println(s1);
		s1.addObject("*abc.a", 3);
		System.out.println(s1);
		s1.addObject("*abc.b", 4);
		System.out.println(s1);
		s1.addObject("*abc.a", 12321);
		System.out.println(s1);
		s1.addObject("*abc.a", 12322);
		System.out.println(s1);
		s1.addObject("*abc.b", 5);
		System.out.println(s1);

		System.out.println("getList:" + s1.getList("abc"));
		System.out.println("getInteger:" + s1.getInteger("abc.a"));
		System.out.println("getInteger:" + s1.getInteger("abc.b"));
		System.out.println("getInteger(abc[0].a):" + s1.getInteger("abc[0].a"));
		System.out.println("getInteger(abc[1].a):" + s1.getInteger("abc[1].a"));
		System.out.println("getInteger(abc[0].b):" + s1.getInteger("abc[0].b"));
		System.out.println("getInteger(abc[1].b):" + s1.getInteger("abc[1].b"));
		s1.addObject("*abc.b", 6);
		System.out.println(s1);
		s1.addObject("*abc.c", 7);
		System.out.println(s1);
		s1.addObject("*abc.*v1.a", 8);
		System.out.println(s1);
		s1.addObject("*abc.*v2.a", 9);
		System.out.println(s1);
		s1.addObject("*abc.*v1.b", 10);
		System.out.println(s1);
		s1.addObject("*abc.*v2.b", 11);
		System.out.println(s1);
		s1.addObject("*abc.*v1.c", 12);
		System.out.println(s1);
		s1.addObject("*abc.*v1.a", 13);
		System.out.println(s1);
		s1.addObject("*abc.*v1.*t1.a", 14);
		System.out.println(s1);
		System.out.println("getInteger(abc.v1.t1.a):" + s1.getInteger("abc.v1.t1.a"));
		s1.addObject("*abc.*v1.*t1.b", 15);
		System.out.println(s1);
		System.out.println("getInteger(abc.v1.t1.b):" + s1.getInteger("abc.v1.t1.b"));
		s1.addObject("*abc.*v1.*t2.a", 16);
		System.out.println(s1);
		s1.addObject("*abc.*v1.*t2.a", 17);
		System.out.println(s1);
		s1.addObject("*abc.*v1.*t2.*n", 18);
		System.out.println(s1);
		s1.addObject("*abc.*v1.*t2.*n", 19);
		System.out.println(s1);
		System.out.println("getInteger(abc.v1.t2.n):" + s1.getInteger("abc.v1.t2.n"));
		System.out.println("getInteger(abc.v1.t2.n[0]):" + s1.getInteger("abc.v1.t2.n[0]"));
		System.out.println("getInteger(abc.v1.t2.n[1]):" + s1.getInteger("abc.v1.t2.n[1]"));
		// System.out.println("getObject:" + s2.getObject("abc.v2.t1.r1.b"));

		System.out.println("------------------------------------------------------");

		MapDataModel s2 = new MapDataModel();
		s2.addObject("a", 1);
		System.out.println(s2);
		s2.addObject("b", 2);
		System.out.println(s2);
		s2.addObject("c", 3);
		System.out.println(s2);
		s2.addObject("abc.a", 4);
		System.out.println(s2);
		s2.addObject("abc.b", 5);
		System.out.println(s2);
		s2.addObject("abc.c", 6);
		System.out.println(s2);
		s2.addObject("abc.a", 7);
		System.out.println(s2);
		s2.addObject("abc.v1.a", 8);
		System.out.println(s2);
		s2.addObject("abc.v1.b", 9);
		System.out.println(s2);
		s2.addObject("abc.v1.c", 10);
		System.out.println(s2);
		s2.addObject("abc.v2.a", 11);
		System.out.println(s2);
		s2.addObject("abc.v2.b", 12);
		System.out.println(s2);
		s2.addObject("abc.v2.c", 13);
		System.out.println(s2);
		s2.addObject("abc.v2.t1.a", 14);
		System.out.println(s2);
		s2.addObject("abc.v2.t1.r1.a", 15);
		System.out.println(s2);
		s2.addObject("abc.v2.t1.r1.b", 16);
		System.out.println(s2);
		s2.addObject("abc.v2.t1.r1.b", 17);
		System.out.println(s2);
		System.out.println("getList:" + s2.getList("abc.v2.t1.r1.b"));
		System.out.println("getInteger:" + s2.getInteger("abc.v2.t1.r1.b"));
		System.out.println("getInteger:" + s2.getInteger("abc.v2.t1.r1.b[0]"));
		System.out.println("getInteger:" + s2.getInteger("abc.v2.t1.r1.b[1]"));
		// System.out.println("getMap:" + s2.getMap("abc.v2.t1.r1"));

		System.out.println("------------------------------------------------------");
		MapDataModel s3 = new MapDataModel();
		s3.addObject("*n", 1);
		System.out.println(s3);
		s3.addObject("*n", 2);
		System.out.println(s3);

		System.out.println("------------------------------------------------------");
		MapDataModel s4 = new MapDataModel();
		s4.addObject("a", 1);
		System.out.println(s4);
		s4.addObject("b.*a", 2);
		System.out.println(s4);
		s4.addObject("b.*a", 3);
		System.out.println(s4);
		s4.addObject("b.*a", 4);
		System.out.println(s4);
		s4.addObject("b.*b", 5);
		System.out.println(s4);
		s4.addObject("b.*b", 6);
		System.out.println(s4);
		s4.addObject("b.*b", 7);
		System.out.println(s4);
		s4.addObject("b.*c.b", 8);
		System.out.println(s4);
		s4.addObject("b.*c.*d", 9);
		System.out.println(s4);
		s4.addObject("b.*c.*d", 10);
		System.out.println(s4);
		s4.addObject("b.*c.*e.*f", 11);
		System.out.println(s4);
		s4.addObject("b.*c.*e.*f", 12);
		System.out.println(s4);
		s4.addObject("b.*c.*g.*f", 13);
		System.out.println(s4);

		System.out.println("------------------------------------------------------");
		System.out.println(s2.getList("abc.a"));
		System.out.println(s2);
		s2.putObject("abc.a", 99);
		System.out.println(s2);
		s2.putObject("abc.v2.t1.r1.b", 98);
		System.out.println(s2);

		System.out.println("------------------------------------------------------");
		MapDataModel s5 = new MapDataModel();
		s1.addObject("a", 1);
		System.out.println(s5);
		s5.addObject("*abc.a", 2);
		System.out.println(s5);
		s5.addObject("*abc.a", 3);
		System.out.println(s5);
		s5.addObject("*abc.b", 4);
		System.out.println(s5);
		s5.addObject("*abc.b", 5);
		System.out.println(s5);
		s5.addObject("*abc.b", 6);
		System.out.println(s5);
		s5.addObject("*abc.c", 7);
		System.out.println(s5);
		s5.putObject("*abc.a", 99);
		System.out.println(s5);
		s5.putObject("*abc", 98);
		System.out.println(s5);

		System.out.println("------------------------------------------------------");

		MapDataModel s6 = new MapDataModel();
		s6.addObject("a", 1);
		s6.addObject("a", 2);
		s6.addObject("a", 3);
		s6.addObject("a", 4);
		s6.addObject("a", 5);
		s6.addObject("b.a", 6);
		s6.addObject("b.a", 7);
		s6.addObject("b.a", 8);
		s6.addObject("b.a", 9);
		s6.addObject("b.a", 10);
		s6.addObject("*c", 11);
		s6.addObject("*c", 12);
		s6.addObject("*c", 13);
		s6.addObject("d.*a", 14);
		s6.addObject("d.*a", 15);
		s6.addObject("d.*a", 16);
		System.out.println(s6);

		System.out.println("------------------------------------------------------");

		s6 = new MapDataModel();
		s6.putObject("a", 1);
		s6.putObject("a", 2);
		s6.putObject("a", 3);
		s6.putObject("a", 4);
		s6.putObject("a", 5);
		s6.putObject("b.a", 6);
		s6.putObject("b.a", 7);
		s6.putObject("b.a", 8);
		s6.putObject("b.a", 9);
		s6.putObject("b.a", 10);
		s6.putObject("c", 11);
		System.out.println(s6);

		// String str1 = "{\"pushTitle\":\"老婆你好哦\",\"pushContent\":\"\"}";
		// MapDataModel s7 = TypeUtil.jsonToObj(str1, MapDataModel.class);
		// System.out.println(s7);

		System.out.println("------------------------------------------------------");

		MapDataModel s8 = new MapDataModel();
		s8.addObject("*userList.*laber.laberId", 1);
		s8.addObject("*userList.*laber.laberName", "a");
		s8.addObject("*userList.*laber.laberId", 2);
		s8.addObject("*userList.*laber.laberName", "b");
		s8.addObject("*userList.*laber.laberId", 3);
		s8.addObject("*userList.*laber.laberName", "c");
		System.out.println(s8);

		System.out.println("------------------------------------------------------");

		// MapDataModel s9 = new MapDataModel();
		// SessionUser s11 = new SessionUser();
		// s11.setUserId(1);
		// s9.putObject("a1", s11);
		// SessionUser sl2 = new SessionUser();
		// sl2.setUserId(2);
		// s9.putObject("a2", sl2);
		// s9.putObject("a3", sl2);
		// SessionUser sl3 = new SessionUser();
		// sl3.setUserId(4);
		// s9.putObject("a4.a", sl3);
		//
		// System.out.println(s9);
		// SessionUser t1 = (SessionUser) s9.getObject("a1");
		// System.out.println(t1.getUserId());
		// SessionUser t2 = (SessionUser) s9.getObject("a4.a");
		// System.out.println(t2.getUserId());
		// SessionUser t2_1 = s9.getObject("a4.a", SessionUser.class);
		// System.out.println(t2_1.getUserId());
		//
		// System.out.println("------------------------------------------------------");
		//
		// MapDataModel s10 = new MapDataModel();
		// SessionUser sr1 = new SessionUser();
		// sr1.setUserId(1);
		// SessionUser sr2 = new SessionUser();
		// sr2.setUserId(2);
		// SessionUser sr3 = new SessionUser();
		// sr3.setUserId(3);
		// s10.addObject("a", sr1);
		// s10.addObject("a", sr2);
		// s10.addObject("a", sr3);
		// s10.addObject("a", sr3);
		// s10.addObject("a", sr3);
		// s10.addObject("b.a", sr1);
		// s10.addObject("b.a", sr2);
		// s10.addObject("b.a", sr3);
		// s10.addObject("b.a", 8);
		// s10.addObject("b.a", sr3);
		// s10.addObject("*c", sr1);
		// s10.addObject("*c", sr2);
		// s10.addObject("*c", sr3);
		// s10.addObject("d.*a", sr1);
		// s10.addObject("d.*a", sr2);
		// s10.addObject("d.*a", sr3);
		// System.out.println(s10);
	}

    
}
