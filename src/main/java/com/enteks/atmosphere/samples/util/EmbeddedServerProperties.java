package com.enteks.atmosphere.samples.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class EmbeddedServerProperties extends Properties {
    private static final String PROPERTIES = "/server.properties";

    private Properties properties = new Properties();

    public Map getProperties() {
        InputStream stream = this.getClass().getResourceAsStream(PROPERTIES);
        try {
            properties.load(stream);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return Collections.unmodifiableMap(properties);
    }

}
