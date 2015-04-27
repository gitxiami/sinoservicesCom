package com.sinoservices.common.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sinoservices.common.util.JsonHelper;

/**
 * 实体对象基类
 * 
 * @author vernon.ye
 * 
 */
public class Entity implements Serializable {

	/**
	 * 序列化版本标识
	 */
	private static final long serialVersionUID = 1L;

	public Entity() {
	}

	/**
	 * 将JSON格式字符串反序列化成实体对象
	 * 
	 * @param json
	 *            SON格式字符串
	 * @param clazz
	 *            实体类型
	 * @return 实体对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return JsonHelper.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将JSON格式字符串反序列化成实体对象
	 * 
	 * @param json
	 *            SON格式字符串
	 * @param clazz
	 *            实体类型
	 * @return 实体对象
	 */
	public static <T> T fromJson(String json, Type clazz) {
		try {
			return JsonHelper.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将JSON格式字符串反序列化成实体对象
	 * 
	 * @param json
	 *            SON格式字符串
	 * @param clazz
	 *            实体类型
	 * @return 实体对象
	 */
	public static <T> ArrayList<T> fromArrayJson(String json, Type clazz) {
		ArrayList<T> entity = JsonHelper.fromJson(json, clazz);
		return entity;
	}

	/**
	 * 将实体对象序列化成JSON格式字符串
	 * 
	 * @param entity
	 *            实体对象
	 * @return JSON格式字符串
	 */
	public static <T> String toJson(T entity) {
		return JsonHelper.toJson(entity);
	}

	/**
	 * 将实体对象的所有属性转换成Map<String, Object>格式
	 * 
	 * @param entity
	 *            实体对象
	 * @return Map对象
	 */
	public static <T> Map<String, Object> toMap(T entity) {
		if (entity == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field f : fields) {
			try {
				f.setAccessible(true);
				Object o = f.get(entity);
				map.put(f.getName(), o);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return map;
	}

	/**
	 * 将实体对象的指定属性转换成Map<String, Object>格式
	 * 
	 * @param entity
	 *            实体对象
	 * @param fields
	 *            指定属性名称列表
	 * @return Map对象
	 */
	public static <T> Map<String, Object> toMap(T entity, String[] fields) {
		if (entity == null || fields == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		Field f = null;
		for (String field : fields) {
			try {
				f = entity.getClass().getDeclaredField(field);
				f.setAccessible(true);
				Object o = f.get(entity);
				map.put(field, o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 获取实体对象指定属性名的值
	 * 
	 * @param entity
	 *            实体对象
	 * @param name
	 *            属性名
	 * @return 属性值
	 */
	public static <T> Object getValue(T entity, String name) {
		if (entity == null || name == null)
			return null;
		Object o = null;
		try {
			Field f = entity.getClass().getDeclaredField(name);
			f.setAccessible(true);
			o = f.get(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o;
	}

	/**
	 * 将实体对象序列化成JSON格式字符串
	 * 
	 * @return JSON格式字符串
	 */
	public String toJson() {
		return JsonHelper.toJson(this);
	}

	/**
	 * 将实体对象转换成Map<String, Object>对象
	 * 
	 * @return Map对象
	 */
	public Map<String, Object> toMap() {
		return toMap(this);
	}

	/**
	 * 将实体对象转换成Map<String, Object>对象
	 * 
	 * @param fields
	 *            指定属性名称列表
	 * @return Map对象
	 */
	public Map<String, Object> toMap(String[] fields) {
		return toMap(this, fields);
	}

	/**
	 * 获取实体对象指定属性名的值
	 * 
	 * @param name
	 *            属性名
	 * @return
	 */
	public Object getFieldValue(String name) {
		return getValue(this, name);
	}

	/**
	 * 获取实体主键编号
	 * 
	 * @param def
	 *            默认值
	 * @return
	 */
	public long getModelId(long def) {
		return def;
	}


}
