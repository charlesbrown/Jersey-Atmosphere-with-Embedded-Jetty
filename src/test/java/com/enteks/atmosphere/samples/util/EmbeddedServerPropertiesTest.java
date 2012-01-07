package com.enteks.atmosphere.samples.util;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static junit.framework.Assert.*;

/**
 * Date: 1/4/12
 * Time: 8:21 PM
 */
public class EmbeddedServerPropertiesTest extends Mockito {

    @Test
    public void testGetProperties() throws Exception {
        EmbeddedServerProperties properties = new EmbeddedServerProperties();
        Map<?,?> testProperties = properties.getProperties();
        assertNotNull(testProperties);
        assertEquals("2880",testProperties.get("jetty.port"));
    }
    
    @Test
    public void testGetPropertiesThrowsIOException() throws Exception {
        InputStream testStream = mock(InputStream.class);
        Properties mockProps = mock(Properties.class);
        IOException myException = null;
        doThrow(new IOException()).when(mockProps).load(testStream);
        try {
            mockProps.load(testStream);
            fail();
        }
        catch (IOException ioex) {
            myException = ioex;
        }
        assertNotNull(myException);

    }
}
