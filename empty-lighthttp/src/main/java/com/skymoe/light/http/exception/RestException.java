package com.skymoe.light.http.exception;

/**
 * 自定义Rest错误对象
 */
public class RestException extends  RuntimeException{
    private static final long serialVersionUID = 1L;

    public RestException(String message) {
        super(message);
    }

    public RestException(Exception e) {
        super(e.getMessage());
    }

    public RestException(String message, Throwable cause) {
        super(message,  cause);
    }
}
