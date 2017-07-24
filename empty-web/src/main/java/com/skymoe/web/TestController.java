package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.model.User;

/**
 * Created by Administrator on 2017/7/24.
 */
@Rest
public class TestController {

    @Rest(path = "/",method = RequestMethod.GET)
    public String index(){

        return "nihao";
    }
}
