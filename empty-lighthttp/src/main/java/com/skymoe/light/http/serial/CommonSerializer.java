package com.skymoe.light.http.serial;

import com.google.gson.Gson;
import com.skymoe.light.http.enums.SerialType;
import com.skymoe.light.http.util.GsonBuilderProxy;

/**
 * 通用序列化
 */
public class CommonSerializer implements  IObjectSerializer{

    private Gson gson;

    public CommonSerializer() {

        this.gson = new GsonBuilderProxy().create();
    }

    @Override
    public String serial(Object obj, SerialType type) {
        if(type == SerialType.XML){

        }
        return null;
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public Object fromJson(String json, Class<?> clz) {
        return null;
    }
}
