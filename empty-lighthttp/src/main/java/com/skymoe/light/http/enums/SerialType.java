package com.skymoe.light.http.enums;

/**
 * 输出格式
 */
public enum SerialType {
    JSON("text/json;"),
    XML("text/xml;");

    public String type() {
        return this.type;
    }
    private String type;
    SerialType(String type) {
        this.type  =type;
    }
}
