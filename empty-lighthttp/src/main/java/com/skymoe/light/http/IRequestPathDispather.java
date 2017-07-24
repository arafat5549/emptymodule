package com.skymoe.light.http;

import com.skymoe.light.http.request.LightHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 请求分发器
 *
 */
public interface IRequestPathDispather {

	/**
	 * 根据请求的path分发请求
	 * 
	 * @param request
	 * @return
	 */
	public String dispatch(LightHttpRequest lightrequest, HttpRequest request);

	/**
	 * 响应的Content-Type
	 * 
	 * @return
	 */
	public String getContentType(HttpRequest request);

}