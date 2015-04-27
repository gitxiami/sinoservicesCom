package com.sinoservices.common.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;

/**
 * JSON���������࣬����GSON�������
 */
public class JsonHelper {

	protected static final Gson gson = new Gson();
	public JsonHelper() {
		// TODO Auto-generated constructor stub
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
	
	public static<T> T fromJson(String json,Type type){
		return gson.fromJson(json, type);
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
