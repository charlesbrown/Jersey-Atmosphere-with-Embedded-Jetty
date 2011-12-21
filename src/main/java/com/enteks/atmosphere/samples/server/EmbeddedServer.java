package com.enteks.atmosphere.samples.server;

import com.enteks.atmosphere.samples.util.EmbeddedServerProperties;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.atmosphere.container.JettyCometSupportWithWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class EmbeddedServer extends Server {

    private static final Map properties = new EmbeddedServerProperties().getProperties();
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);
    
    private static final String ATMOSPHERE_RESOURCES = "com.enteks.atmosphere.samples.resources.atmosphere";
    private static final String JERSEY_RESOURCES = "com.enteks.atmosphere.samples.resources.jersey";
    private static final String DEFAULT_MAX_INACTIVITY_LIMIT = String.valueOf(properties.get("atmosphere.service.maxinactivitylimit"));
    private static final String DEFAULT_PORT = String.valueOf(properties.get("jetty.port"));
    
    private static Server webServer;

    @SuppressWarnings("unused")
    public void startServer() throws Exception {
        startServer(DEFAULT_PORT, DEFAULT_MAX_INACTIVITY_LIMIT);
    }
    
    public void startServer(String port, String maxInactivityLimit) throws Exception {
        int portAsInt = (!port.isEmpty()) ? Integer.valueOf(port):Integer.valueOf(DEFAULT_PORT);
        webServer = new Server(portAsInt);
        logger.info("Starting EmbeddedServer...");

        ServletHolder atmosphereServletHolder = initAtmosphereServlet(maxInactivityLimit);
        ServletHolder jerseyServletHolder = initJerseyServlet();

        ServletContextHandler servletContextHandler;
        servletContextHandler = new ServletContextHandler(webServer, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(atmosphereServletHolder, "/atmosphere/*");
        servletContextHandler.addServlet(jerseyServletHolder, "/jersey/*");

        webServer.start();
        webServer.join();

        logger.info("Started EmbeddedServer");
    }

    private ServletHolder initJerseyServlet() {
        ServletHolder jerseyServletHolder = new ServletHolder(ServletContainer.class);
        jerseyServletHolder.setInitParameter("com.sun.jersey.config.property.packages", JERSEY_RESOURCES);
        return jerseyServletHolder;
    }

    private ServletHolder initAtmosphereServlet(String maxInactivityLimit) {
        ServletHolder atmosphereServletHolder = new ServletHolder(AtmosphereServlet.class);
        atmosphereServletHolder.setInitParameter("com.sun.jersey.config.property.packages", ATMOSPHERE_RESOURCES);
        atmosphereServletHolder.setInitParameter("com.sun.jersey.spi.container.ResourceFilter", "org.atmosphere.core.AtmosphereFilter");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.PROPERTY_COMET_SUPPORT, JettyCometSupportWithWebSocket.class.getName());
        atmosphereServletHolder.setInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.PROPERTY_NATIVE_COMETSUPPORT, "true");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.MAX_INACTIVE, (maxInactivityLimit.isEmpty()) ? DEFAULT_MAX_INACTIVITY_LIMIT : maxInactivityLimit);
        return atmosphereServletHolder;
    }

    @SuppressWarnings("unused")
    public void stopServer() throws Exception {
        logger.info("Stopping EmbeddedServer...");
        webServer.stop();
        logger.info("EmbeddedServer Stopped with stopServer() method.");
    }

}
