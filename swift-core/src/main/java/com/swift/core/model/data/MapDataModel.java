/*
 * @(#)MapDataModel.java   1.0  2016年4月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.model.data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
	    Object value = getObjectInternal(objectPath, this);
	    if(value==null) return null;
	    if(value.getClass().equals(classType)) {
            return (T)value;
        }else {
            return JsonUtil.toObj(JsonUtil.toJson(value), classType);
        }
	}

	/**
	 * @see com.hhmk.hospital.common.model.service.data.DataModel#getList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<DataModel> getList(String objectPath) {
		return (List<DataModel>)(Object)getListInternal(objectPath, MapDataModel.class);
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

	@SuppressWarnings({ "rawtypes" })
	protected Object getObjectInternal(String path, Object parent) {
		if (path == null || EMPTY.equals(path)) {
			throw new RuntimeException("path must not be null");
		}

		int idx = path.indexOf(DOT);
		if (idx == -1) {
			Object v = getPropertyValue(path, parent);
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
		Object o = getPropertyValue(name, parent);
		if (o == null) {
			return null;
		}
		if (o instanceof List) {
		    int i = DataModelUtil.getIndex(DataModelUtil.stripAsterisk(name));
		    if(i==-1)
		        return getObjectFromList(subPath, (List) o);
		    else
		        return getObjectInternal(subPath, ((List) o).get(i));
        }
        return getObjectInternal(subPath, o);
		
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
            Object value = ((Map) obj).get(DataModelUtil.normalize(propertyName));
            int i = DataModelUtil.getIndex(DataModelUtil.stripAsterisk(propertyName));
            if(i==-1) {
                return value;
            } else {
                if(value instanceof List) {
                    return ((List)value).get(i);
                }else {
                    return value;
                }
            }
        }
        return obj;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private Object getObjectFromList(String propertyName, List list) {
        if(list == null) return null;
        int i = DataModelUtil.getIndex(DataModelUtil.stripAsterisk(propertyName));
        if (i == -1) {
            List newList = new LinkedList();
            for(Object obj:list) {
                if(obj instanceof Map)
                    newList.add(getObjectInternal(propertyName, obj));
                else
                    newList.add(obj);
            }
            if(newList.size()==1) return newList.get(0);
            return newList;
        }
        i = Math.max(i, 0);
        i = Math.min(i, list.size());
        return getObjectInternal(propertyName, list.get(i));
    }

	@SuppressWarnings("unchecked")
	protected <T> List<T> getListInternal(String path, Class<T> clazz) {
		Object value = getObjectInternal(path, this);
		if (TypeUtil.isNull(value)) {
			return new ArrayList<T>();
		} else if (value instanceof List) {
		    Object obj = ((List<?>)value).get(0);
		    if(clazz.isAssignableFrom(obj.getClass())) {
		        return (List<T>) value;
		    }else {
		       return JsonUtil.toListObject(JsonUtil.toJson(value), clazz);
		    }
		}else {
		    if(value.getClass().equals(clazz)) {
		        return Arrays.asList((T)value);
		    }else {
		        return Arrays.asList(JsonUtil.toObj(JsonUtil.toJson(value), clazz));
		    }
		}
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
					return;
				}
			}
			parent.remove(DataModelUtil.normalize(path));
			return;
		}
		String name = path.substring(0, idx);
		path = path.substring(idx + 1);
		Object value = parent.get(DataModelUtil.normalize(name));
		if(value==null) {
		    parent.remove(DataModelUtil.normalize(name));
		    return;
		}
		if (value instanceof Map) {
			remove(path, (Map<String, Object>) value);
			if(((Map<String, Object>) value).isEmpty()) {
			    parent.remove(DataModelUtil.normalize(name));
	            return;
			}
		}
		if (value instanceof List) {
		    List<Object> list = (List<Object>) value;
		    int i = DataModelUtil.getIndex(name);
		    if(i==-1) {
    		    List mycopy=new ArrayList(Arrays.asList(new Object[list.size()]));
    		    Collections.copy(mycopy, (List)value);
                for(Object obj:mycopy) {
                    if (obj instanceof Map) {
                        remove(path, (Map<String, Object>) obj);
                        if(((Map<String, Object>) obj).isEmpty()) {
                            list.remove(obj);
                        }
                    }
                }
		    }else {
		        i = Math.max(i, 0);
                i = Math.min(i, list.size() - 1);
                if (list.get(i) instanceof Map) {
                    remove(path, (Map<String, Object>)list.get(i));
                }
		    }
            if(list.isEmpty()) {
                parent.remove(DataModelUtil.normalize(name));
                return;
            }
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

}
