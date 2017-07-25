package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.model.Pet;

/**
 * Created by Administrator on 2017/7/25.
 */
@Rest(path="/pet")
public class PetController {

    //POST /pet 添加一个新宠物到商店！
    @Rest(path="/",method = RequestMethod.POST)
    public Pet addPet(Pet pet){
        return pet;
    }

}
