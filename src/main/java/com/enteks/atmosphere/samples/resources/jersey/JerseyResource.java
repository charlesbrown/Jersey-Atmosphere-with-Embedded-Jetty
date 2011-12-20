package com.enteks.atmosphere.samples.resources.jersey;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * User: charles.brown
 * Date: 12/20/11
 * Time: 7:46 AM
 */
@Path("/jersey")
public class JerseyResource {
    
    @GET
    @Path("/get")
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet() {
        return "This is the plain Jersey GET method.";
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String doPost(@FormParam("message") String message) {
        return "You posted this: "+message;
    }
}
