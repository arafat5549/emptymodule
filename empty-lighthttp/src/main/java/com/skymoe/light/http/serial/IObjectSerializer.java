package com.skymoe.light.http.serial;

import com.skymoe.light.http.enums.SerialType;

/**
 * Object to string 序列器
 * 
 *
 */
public interface IObjectSerializer {

	/**
	 * 序列化的Media-Type；输出的格式类型
	 * @param type 序列化类型
	 * @return
	 */
	String getMediaType(SerialType type);

	/**
	 * 序列化对象
	 *
	 * @param obj
	 * @return
	 */
	String serialize(Object obj, SerialType type);
	/**
	 * 反序列化
	 * @param content
	 * @return
	 */
	Object deserialize(String content,Class<?> clz,SerialType type);
}
