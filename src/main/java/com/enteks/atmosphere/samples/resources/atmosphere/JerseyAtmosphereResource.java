package com.enteks.atmosphere.samples.resources.atmosphere;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.jersey.Broadcastable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class JerseyAtmosphereResource {


    /*
     * Subscribes listener to the broadcast of clicker responses
     * @PARAM {channel} is the path parameter representing the unique ID generated when the
     * listener registered with the service.
     */
    @GET
    @Path("/{channel}")
    @Suspend(listeners = {AtmosphereListener.class}, outputComments = true)
    public Broadcastable subscribe(@PathParam("channel") Broadcaster channel) {
        return new Broadcastable(channel);
    }

    /*
     * Receives responses from the SoftClicker and broadcasts the responses to
     * subscribed listeners.
     */
    @POST
    @Path("/{channel}/response")
    @Broadcast
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Broadcastable broadcastStudentResponse (
            @PathParam("channel") Broadcaster channel,
            @FormParam("message") String message
    )
    {
        return new Broadcastable(message,"thisMessageShouldBeReturnedToCaller", channel);
    }
}
