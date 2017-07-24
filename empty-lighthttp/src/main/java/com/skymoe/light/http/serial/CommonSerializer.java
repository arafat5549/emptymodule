package com.skymoe.light.http.serial;

import com.google.gson.Gson;
import com.skymoe.light.http.enums.SerialType;
import com.skymoe.light.http.util.GsonBuilderProxy;
import com.skymoe.light.http.util.XmlMapper;

/**
 * 通用序列化
 *
 * 暂时只支持JSON和XML类型
 *
 * 如果需要支持其他的类型比如SOAP 、WEBSERVICE 需要添加
 */
public class CommonSerializer implements  IObjectSerializer{

    private Gson gson;

    public CommonSerializer() {

        this.gson = new GsonBuilderProxy().create();
    }


   //最终response 输出的类型
    @Override
    public String getMediaType(SerialType type) {
        if(type == SerialType.XML){
           return "text/xml;";
        }
        return "text/json;";
    }
    @Override
    public String serialize(Object obj, SerialType type) {
        if(type == SerialType.XML){
            return XmlMapper.toXml(obj);
        }
        return gson.toJson(obj);
    }
    @Override
    public Object deserialize(String content, Class<?> clz,SerialType type) {
        if(type == SerialType.XML){
            return XmlMapper.fromXml(content,clz);
        }
        return this.gson.fromJson(content,clz);
    }
}
