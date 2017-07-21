package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.model.User;
import org.springframework.stereotype.Controller;

/**
 * Created by Administrator on 2017/7/21.
 */
@Controller
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
}
