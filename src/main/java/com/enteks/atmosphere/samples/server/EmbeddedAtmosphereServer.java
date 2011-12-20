package com.enteks.atmosphere.samples.server;

import com.enteks.atmosphere.samples.util.EmbeddedServerProperties;
import org.atmosphere.container.JettyCometSupportWithWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class EmbeddedAtmosphereServer extends Server {

    private static final Map properties = new EmbeddedServerProperties().getProperties();
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedAtmosphereServer.class);
    
    private static final String RESOURCES = "com.enteks.atmosphere.samples.resources.atmosphere";
    private static final String DEFAULT_MAX_INACTIVITY_LIMIT = String.valueOf(properties.get("atmosphere.service.maxinactivitylimit"));
    private static final String DEFAULT_PORT = String.valueOf(properties.get("jetty.port"));
    
    private static Server webServer;

    public void startServer() throws Exception {
        startServer(DEFAULT_PORT, DEFAULT_MAX_INACTIVITY_LIMIT);
    }
    
    public void startServer(String port, String maxInactivityLimit) throws Exception {
        webServer = new Server(2880);
        logger.info("Starting EmbeddedAtmosphereServer...");

        ServletHolder holder = new ServletHolder(AtmosphereServlet.class);
        holder.setInitParameter("com.sun.jersey.config.property.packages", RESOURCES);
        holder.setInitParameter("com.sun.jersey.spi.container.ResourceFilter", "org.atmosphere.core.AtmosphereFilter");
        holder.setInitParameter(ApplicationConfig.PROPERTY_COMET_SUPPORT, JettyCometSupportWithWebSocket.class.getName());
        holder.setInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        holder.setInitParameter(ApplicationConfig.PROPERTY_NATIVE_COMETSUPPORT, "true");
        holder.setInitParameter(ApplicationConfig.MAX_INACTIVE, (maxInactivityLimit.isEmpty())? DEFAULT_MAX_INACTIVITY_LIMIT:maxInactivityLimit);

        ServletContextHandler servletContextHandler;
        servletContextHandler = new ServletContextHandler(webServer, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(holder, "/*");

        webServer.start();
        webServer.join();

        logger.info("Started EmbeddedAtmosphereServer");
    }

    @SuppressWarnings("unused")
    public void stopServer() throws Exception {
        logger.info("Stopping EmbeddedAtmosphereServer...");
        webServer.stop();
        logger.info("EmbeddedAtmosphereServer Stopped with stopServer() method.");
    }

}
