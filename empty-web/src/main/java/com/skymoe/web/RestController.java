package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.light.http.enums.SerialType;
import com.skymoe.light.http.netty.ChannelContext;
import com.skymoe.model.User;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 测试各种RestController风格的功能
 */
//@Controller
@Rest(path="/rest")
public class RestController {

    @Rest(path = "/user")
    public User user() {
        System.out.println("触发:RestController#user");
        User user = new User(1,"My-Project v0.0.1 www.ximalaya.com");
        return user;
    }

    @Rest(path = "/user2")
    public User user2() {
        System.out.println("触发:RestController#user2");
        User user = new User(1,"RestController");
        return user;
    }


    /**
     * 测试各种功能
     * @return
     */
    //测试POST方法
    @Rest(path = "/post",method = RequestMethod.POST)
    public User postUser(){
        User user = new User(11,"RestControllerPOST");
        return user;
    }
    //测试GET方法 以及 默认为JSON传输
    @Rest(path = "/get",method = RequestMethod.GET)
    public User getUser(){
        User user = new User(12,"RestControlleGET");
        return user;
    }

    /**
     * 简单用法说  假设你的url路径为http://localhost:9090/rest/xml?[method]=1
     *
     * 1.利用你的方法参数 注意名称必须跟你的url的方法名一致 我们这里都是method
     * 你的方法为 xxx(Long [method])  两个带中括号的method必须一致
     *
     * 2.可以利用 xml或者json格式传输对象# 你的方法为xml传输则你的对象也为XML
     *
     *  XML方式：
     *  http://localhost:9090/rest/xml?method=111&child=<user><id>1212</id><name>小孩子</name></user>
     *
     *  JSON方式：
     *  http://localhost:9090/rest/xml?method=111&child={%22id%22:12,%22name%22:%22RestControlleGET%22}
     */

    //测试XML传输
    //测试参数名的映射  暂时支持 int/long/string/boolean/double/Bigdecimal类型  以及对应的复合类型（.可以利用 xml或者json格式传输对象# 你的方法为xml传输则你的对象也为XML）
    //参数名映射注意必须使用Integer等封装类型 不然反射默认为null会报错
    @Rest(path = "/xml",method = RequestMethod.GET,serial = SerialType.XML)
    public User getUserXML(Integer method,String name,User child){
        System.out.print("method="+method+","+"name="+name+",");
        System.out.println("child="+child);
        User user = new User(method,name);
        user.setChild(child);

        System.out.println(ChannelContext.getClientIp());
        return user;
    }
}
