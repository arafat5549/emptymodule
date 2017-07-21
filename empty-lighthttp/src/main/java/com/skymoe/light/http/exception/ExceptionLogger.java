package com.skymoe.light.http.exception;

/**
 * 异常处理
 * 
 *
 */
public interface ExceptionLogger {

	void log(String message, Throwable cause);

}
