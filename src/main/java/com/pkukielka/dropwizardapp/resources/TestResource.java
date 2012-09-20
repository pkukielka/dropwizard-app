package com.pkukielka.dropwizardapp.resources;

import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
    @GET
    @Timed
    public Response test() {
        return Response.ok().type(MediaType.APPLICATION_JSON).build();
    }
}
