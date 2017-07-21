package com.skymoe.light.http.serial;

import com.skymoe.light.http.enums.SerialType;

/**
 * Object to string 序列器
 * 
 *
 */
public interface IObjectSerializer {

	/**
	 * 序列化对象
	 * 
	 * @param obj
	 * @return
	 */
	String serial(Object obj, SerialType type);

	/**
	 * 序列化的Media-Type
	 * 
	 * @return
	 */
	String getMediaType();
	/**
	 * JSON数据
	 * @param json
	 * @return
	 */
	Object fromJson(String json,Class<?> clz);
}
