package com.enteks.atmosphere.samples.server;

import com.enteks.atmosphere.samples.util.EmbeddedServerProperties;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.atmosphere.container.JettyCometSupportWithWebSocket;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.eclipse.jetty.http.ssl.SslContextFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.util.Map;

public final class EmbeddedServer extends Server {

    private static final Map properties = new EmbeddedServerProperties().getProperties();
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);
    
    private static final String ATMOSPHERE_RESOURCES = "com.enteks.atmosphere.samples.resources.atmosphere";
    private static final String JERSEY_RESOURCES = "com.enteks.atmosphere.samples.resources.jersey";
    private static final String DEFAULT_MAX_INACTIVITY_LIMIT = String.valueOf(properties.get("atmosphere.service.maxinactivitylimit"));
    private static final String DEFAULT_PORT = String.valueOf(properties.get("jetty.port"));
    private static final String DEFAULT_SSL_PORT = String.valueOf(properties.get("jetty.ssl.port"));
    private static final String SSL_KEYSTORE_FILE = String.valueOf(properties.get("jetty.ssl.keystore"));
    private static final String SSL_KEYSTORE_PASS = String.valueOf(properties.get("jetty.ssl.keystore.storepass"));
    
    private static Server webServer;

    @SuppressWarnings("unused")
    public void startServer() throws Exception {
        startServer(DEFAULT_PORT, DEFAULT_SSL_PORT, DEFAULT_MAX_INACTIVITY_LIMIT);
    }
    
    public void startServer(String port, String sslPort, String maxInactivityLimit) throws Exception {
        webServer = new Server();
        logger.info("Starting EmbeddedServer...");

        SelectChannelConnector selectChannelConnector = new SelectChannelConnector();
        selectChannelConnector.setPort((port.isEmpty()) ? Integer.valueOf(DEFAULT_PORT) : Integer.valueOf(port));

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePassword(SSL_KEYSTORE_PASS);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(this.getClass().getResourceAsStream(SSL_KEYSTORE_FILE), SSL_KEYSTORE_PASS.toCharArray());
        sslContextFactory.setKeyStore(keyStore);

        SslSelectChannelConnector sslSelectChannelConnector = new SslSelectChannelConnector(sslContextFactory);
        sslSelectChannelConnector.setPort((sslPort.isEmpty() ? Integer.valueOf(DEFAULT_SSL_PORT) : Integer.valueOf(sslPort)));

        Connector[] connectors = new Connector[] {selectChannelConnector, sslSelectChannelConnector};

        webServer.setConnectors(connectors);

        ServletHolder atmosphereServletHolder = initAtmosphereServlet(maxInactivityLimit);
        ServletHolder jerseyServletHolder = initJerseyServlet();
        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST");

        ServletContextHandler servletContextHandler;
        servletContextHandler = new ServletContextHandler(webServer, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(atmosphereServletHolder, "/atmosphere/*");
        servletContextHandler.addServlet(jerseyServletHolder, "/jersey/*");
        servletContextHandler.addFilter(filterHolder, "/*", null);

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
