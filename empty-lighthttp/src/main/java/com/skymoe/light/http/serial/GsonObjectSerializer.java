package com.skymoe.light.http.serial;

import com.skymoe.light.http.util.GsonBuilderProxy;
import com.google.gson.Gson;

/**
 * Gson 序列化实现
 * 实现JSON解析
 */
public class GsonObjectSerializer implements IObjectSerializer {

	private static final String TEXT_JSON = "text/json;";
	private Gson gson;

	public GsonObjectSerializer() {
		this.gson = new GsonBuilderProxy().create();
	}

	@Override
	public String serial(Object obj) {
		return this.gson.toJson(obj);
	}

	@Override
	public String getMediaType() {
		return TEXT_JSON;
	}

	@Override
	public Object fromJson(String json,Class<?> clz) {
		return this.gson.fromJson(json,clz);
	}



}
