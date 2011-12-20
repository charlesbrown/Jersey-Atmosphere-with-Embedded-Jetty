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

public final class EmbeddedJerseyServer extends Server {

    private static final Map properties = new EmbeddedServerProperties().getProperties();
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJerseyServer.class);
    
    private static final String RESOURCES = "com.enteks.atmosphere.samples.resources.jersey";
    private static final String DEFAULT_PORT = String.valueOf(properties.get("jetty.port"));
    
    private static Server webServer;

    public void startServer() throws Exception {
        startServer(DEFAULT_PORT);
    }
    
    public void startServer(String port) throws Exception {
        webServer = new Server(Integer.valueOf(port));
        logger.info("Starting EmbeddedJerseyServer...");

        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter("com.sun.jersey.config.property.packages", RESOURCES);

        ServletContextHandler servletContextHandler;
        servletContextHandler = new ServletContextHandler(webServer, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(holder, "/*");

        webServer.start();
        webServer.join();

        logger.info("Started EmbeddedJerseyServer");
    }

    @SuppressWarnings("unused")
    public void stopServer() throws Exception {
        logger.info("Stopping EmbeddedJerseyServer...");
        webServer.stop();
        logger.info("EmbeddedJerseyServer Stopped with stopServer() method.");
    }

}
