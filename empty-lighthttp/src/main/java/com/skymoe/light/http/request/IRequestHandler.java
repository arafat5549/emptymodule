package com.skymoe.light.http.request;


/**
 * 实现此接口，注册请求的处理业务
 *
 */
public interface IRequestHandler <T> {

	String ENCODING = "UTF-8";

	T handle(LightHttpRequest request);

}
