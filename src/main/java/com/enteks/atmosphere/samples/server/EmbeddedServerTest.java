package com.enteks.atmosphere.samples.server;

/**
 * User: charles.brown
 * Date: 12/13/11
 * Time: 8:24 AM
 */
public class EmbeddedServerTest {
    
    public static void main(String[] args) throws Exception {
        String port = (args.length > 0) ? args[0]:"";
        String sslPort = (args.length > 1) ? args[1]:"";
        String maxInactivityLimit = (args.length > 2) ? args[2]:"";
        EmbeddedServer server = new EmbeddedServer();
        server.startServer(port, sslPort, maxInactivityLimit);
    }
}
