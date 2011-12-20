package com.enteks.atmosphere.samples.server;

import org.eclipse.jetty.server.Server;

/**
 * User: charles.brown
 * Date: 12/13/11
 * Time: 8:24 AM
 */
public class ServiceMain {
    
    public static void main(String[] args) throws Exception {
        if(args.length > 0) {
            String serverToUse = args[0];
            String port = args[1];
            if(serverToUse.equalsIgnoreCase("atmosphere"))
            {
                EmbeddedAtmosphereServer server = new EmbeddedAtmosphereServer();
                server.startServer(port,"");
            }
            else if(serverToUse.equalsIgnoreCase("jersey"))
            {
                EmbeddedJerseyServer server = new EmbeddedJerseyServer();
                server.startServer(port);
            }
        }
        else
        {
            EmbeddedJerseyServer server = new EmbeddedJerseyServer();
            server.startServer();
        }
    }
}
