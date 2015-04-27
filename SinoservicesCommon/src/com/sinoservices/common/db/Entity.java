package com.sinoservices.common.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sinoservices.common.util.JsonHelper;

/**
 * ʵ��������
 * 
 * @author vernon.ye
 * 
 */
public class Entity implements Serializable {

	/**
	 * ���л��汾��ʶ
	 */
	private static final long serialVersionUID = 1L;

	public Entity() {
	}

	/**
	 * ��JSON��ʽ�ַ��������л���ʵ�����
	 * 
	 * @param json
	 *            SON��ʽ�ַ���
	 * @param clazz
	 *            ʵ������
	 * @return ʵ�����
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
	 * ��JSON��ʽ�ַ��������л���ʵ�����
	 * 
	 * @param json
	 *            SON��ʽ�ַ���
	 * @param clazz
	 *            ʵ������
	 * @return ʵ�����
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
	 * ��JSON��ʽ�ַ��������л���ʵ�����
	 * 
	 * @param json
	 *            SON��ʽ�ַ���
	 * @param clazz
	 *            ʵ������
	 * @return ʵ�����
	 */
	public static <T> ArrayList<T> fromArrayJson(String json, Type clazz) {
		ArrayList<T> entity = JsonHelper.fromJson(json, clazz);
		return entity;
	}

	/**
	 * ��ʵ��������л���JSON��ʽ�ַ���
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return JSON��ʽ�ַ���
	 */
	public static <T> String toJson(T entity) {
		return JsonHelper.toJson(entity);
	}

	/**
	 * ��ʵ��������������ת����Map<String, Object>��ʽ
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return Map����
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
	 * ��ʵ������ָ������ת����Map<String, Object>��ʽ
	 * 
	 * @param entity
	 *            ʵ�����
	 * @param fields
	 *            ָ�����������б�
	 * @return Map����
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
	 * ��ȡʵ�����ָ����������ֵ
	 * 
	 * @param entity
	 *            ʵ�����
	 * @param name
	 *            ������
	 * @return ����ֵ
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
	 * ��ʵ��������л���JSON��ʽ�ַ���
	 * 
	 * @return JSON��ʽ�ַ���
	 */
	public String toJson() {
		return JsonHelper.toJson(this);
	}

	/**
	 * ��ʵ�����ת����Map<String, Object>����
	 * 
	 * @return Map����
	 */
	public Map<String, Object> toMap() {
		return toMap(this);
	}

	/**
	 * ��ʵ�����ת����Map<String, Object>����
	 * 
	 * @param fields
	 *            ָ�����������б�
	 * @return Map����
	 */
	public Map<String, Object> toMap(String[] fields) {
		return toMap(this, fields);
	}

	/**
	 * ��ȡʵ�����ָ����������ֵ
	 * 
	 * @param name
	 *            ������
	 * @return
	 */
	public Object getFieldValue(String name) {
		return getValue(this, name);
	}

	/**
	 * ��ȡʵ���������
	 * 
	 * @param def
	 *            Ĭ��ֵ
	 * @return
	 */
	public long getModelId(long def) {
		return def;
	}


}
