package com.pkukielka.dropwizardapp.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

@Path("/server.json")
@Api(value = "/server", description = "Operations for manipulating Jetty server")
@Produces(MediaType.APPLICATION_JSON)
public class ServerResource extends SwaggerResource {
    final int jettyPort;

    public ServerResource(int jettyPort) {
        this.jettyPort = jettyPort;
    }

    @Timed
    @PUT
    @Path("/shutdown")
    @ApiOperation(value = "Shutdown Jetty", notes = "Shutting down Jetty will cause the entire application to exit")
    public Response shutdown() {
        try {
            URL url = new URL("http://localhost:" + jettyPort + "/shutdown?token=stopJetty");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.getResponseCode();
        } catch (SocketException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Response.ok().build();
    }
}
