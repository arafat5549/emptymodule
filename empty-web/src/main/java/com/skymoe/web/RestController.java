package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.model.User;

/**
 * Created by Administrator on 2017/7/21.
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
    //测试POST方法
    @Rest(path = "/postuser",method = RequestMethod.POST)
    public User postUser(){
        User user = new User(11,"RestControllerPOST");
        return user;
    }
    @Rest(path = "/postuser",method = RequestMethod.GET)
    public User getUser(){
        User user = new User(11,"RestControlleGETr");
        return user;
    }
}
