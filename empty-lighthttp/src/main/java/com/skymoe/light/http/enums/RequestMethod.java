package com.skymoe.light.http.enums;

/**
 * 动作类型
 */
public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    public String type() {
        return this.type;
    }
    private String type;
    private RequestMethod(String type) {
        this.type = type;
    }
}
