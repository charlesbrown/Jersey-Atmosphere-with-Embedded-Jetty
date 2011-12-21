package com.enteks.atmosphere.samples.server;

/**
 * User: charles.brown
 * Date: 12/13/11
 * Time: 8:24 AM
 */
public class EmbeddedServerTest {
    
    public static void main(String[] args) throws Exception {
        String port = "";
        if(args.length > 0) {
            port = args[0];
        }
        EmbeddedServer server = new EmbeddedServer();
        server.startServer(port,"");
    }
}
