/**
 *  Copyright 2014 Reverb Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.skymoe.swagger.resource;


import com.skymoe.swagger.data.PetData;
import com.skymoe.swagger.model.Pet;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pet")
@Api(value = "/pet", description = "关于宠物对象的操作", authorizations = {
  @Authorization(value = "petstore_auth",
  scopes = {
    @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
    @AuthorizationScope(scope = "read:pets", description = "read your pets")
  })
})
@Produces({"application/json", "application/xml"})
public class PetResource {

  private static final String RESPOSE_CODE_400 = "无效的ID";
  private static final String RESPOSE_CODE_404 = "未找到指定ID的宠物";

  static PetData petData = new PetData();
  static JavaRestResourceUtil ru = new JavaRestResourceUtil();

  //
  @GET
  @Path("/{petId}")
  @ApiOperation(value = "根据ID获取宠物对象",
    notes = "Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions", 
    response = Pet.class,
    authorizations = @Authorization(value = "api_key")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = RESPOSE_CODE_400),
      @ApiResponse(code = 404, message = RESPOSE_CODE_404) })
  public Response getPetById(
      @ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathParam("petId") Long petId)
      throws com.skymoe.swagger.exception.NotFoundException {
    Pet pet = petData.getPetbyId(petId);
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new com.skymoe.swagger.exception.NotFoundException(404, RESPOSE_CODE_404);
    }
  }
  //
  @DELETE
  @Path("/{petId}")
  @ApiOperation(value = "根据ID删除宠物对象")
  @ApiResponses(value = { @ApiResponse(code = 400, message = RESPOSE_CODE_400)})
  public Response deletePet(
    @ApiParam() @HeaderParam("api_key") String apiKey,
    @ApiParam(value = "Pet id to delete", required = true)@PathParam("petId") Long petId) {
    petData.deletePet(petId);
    return Response.ok().build();
  }

  @POST
  @Consumes({"application/json", "application/xml"})
  @ApiOperation(value = "添加一只宠物到宠物商店")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "无效输入") })
  public Response addPet(
      @ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
    Pet updatedPet = petData.addPet(pet);
    return Response.ok().entity(updatedPet).build();
  }

  @PUT
  @Consumes({"application/json", "application/xml"})
  @ApiOperation(value = "更新已经存在的宠物信息")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = RESPOSE_CODE_400),
      @ApiResponse(code = 404, message = RESPOSE_CODE_404),
      @ApiResponse(code = 405, message = "Validation exception") })
  public Response updatePet(
      @ApiParam(value = "宠物对象需要已经存在于宠物商店中", required = true) Pet pet) {
    Pet updatedPet = petData.addPet(pet);
    return Response.ok().entity(updatedPet).build();
  }

  @GET
  @Path("/findByStatus")
  @ApiOperation(value = "Finds Pets by status", 
    notes = "Multiple status values can be provided with comma seperated strings", 
    response = Pet.class, 
    responseContainer = "List")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value") })
  public Response findPetsByStatus(
      @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available", allowableValues = "available,pending,sold", allowMultiple = true) @QueryParam("status") String status) {
    return Response.ok(petData.findPetByStatus(status)).build();
  }

  @GET
  @Path("/findByTags")
  @ApiOperation(value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.", 
    response = Pet.class, 
    responseContainer = "List")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid tag value") })
  @Deprecated
  public Response findPetsByTags(
      @ApiParam(value = "Tags to filter by", required = true, allowMultiple = true) @QueryParam("tags") String tags) {
    return Response.ok(petData.findPetByTags(tags)).build();
  }


  @POST
  @Path("/{petId}")
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @ApiOperation(value = "Updates a pet in the store with form data",
    consumes = MediaType.APPLICATION_FORM_URLENCODED)
  @ApiResponses(value = {
    @ApiResponse(code = 405, message = "Invalid input")})
  public Response  updatePetWithForm (
   @ApiParam(value = "ID of pet that needs to be updated", required = true)@PathParam("petId") String petId,
   @ApiParam(value = "Updated name of the pet", required = false)@FormParam("name") String name,
   @ApiParam(value = "Updated status of the pet", required = false)@FormParam("status") String status) {
    System.out.println(name);
    System.out.println(status);
    return Response.ok().entity(new com.skymoe.swagger.model.ApiResponse(200, "SUCCESS")).build();
  }
}
