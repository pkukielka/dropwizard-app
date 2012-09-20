package com.pkukielka.dropwizardapp.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test.json")
@Api(value = "/test", description = "Operations for testing Dropwizard App")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource extends SwaggerResource {
    @Timed
    @PUT
    @Path("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Basic echo service", notes = "Can be used for checking if resources are working")
    @ApiResponse(value = "String")
    public String test(@ApiParam(value = "Text which will be returned by the echo service", required = true) String message) {
        return message;
    }
}
