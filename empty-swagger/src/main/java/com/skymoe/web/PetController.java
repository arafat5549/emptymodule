package com.skymoe.web;

import com.skymoe.light.http.annotation.Rest;
import com.skymoe.light.http.enums.RequestMethod;
import com.skymoe.swagger.data.PetData;
import com.skymoe.swagger.model.Pet;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/7/25.
 */

@Rest(path="/pet")
public class PetController
{
    static PetData petData = new PetData();

    @Rest(path="",method = RequestMethod.POST)
    public Pet addPet(
            @ApiParam(value = "要添加的宠物对象", required = true) Pet pet) {
        Pet updatedPet = petData.addPet(pet);
        return updatedPet;
    }
}
