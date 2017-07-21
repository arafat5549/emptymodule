package com.skymoe.web;

import com.skymoe.light.http.annotation.RequestBean;
import com.skymoe.light.http.annotation.Rest;
import com.skymoe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */

@Controller
@Rest
public class TestController {



    @Rest(path = "/")
    public String index() {
        System.out.println("触发:index方法");
        return "My-Project v0.0.1 www.ximalaya.com";
    }

    @Rest(path = "/help")
    public Map<String, Method> apiHelp() {
         System.out.println("触发:apiHelp方法");
        // help方法，展现所支持的http方法，也就是api信息
        return null;
    }

    @Rest(path = "/user")
    public User listUser() {
        System.out.println("触发:listUser");
        // 自己封装一些配置参数信息
        User user = new User(1,"wang");
        return user;
    }
}
