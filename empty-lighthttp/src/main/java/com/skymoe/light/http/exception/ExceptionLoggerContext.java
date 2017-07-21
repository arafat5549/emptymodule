package com.skymoe.light.http.exception;

/**
 * 异常处理器注册
 * 
 *
 */
public class ExceptionLoggerContext {

	private static ExceptionLogger exceptionLogger;

	public static void register(ExceptionLogger exceptionLogger) {
		ExceptionLoggerContext.exceptionLogger = exceptionLogger;
	}

	public static ExceptionLogger getExceptionLogger() {
		return exceptionLogger;
	}

}
