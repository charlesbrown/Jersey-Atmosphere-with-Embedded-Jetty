package com.enteks.atmosphere.samples.resources.jersey;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Date: 1/7/12
 * Time: 4:22 PM
 */
public class JerseyResourceTest extends JerseyTest {
    
    private final static String JERSEY_ROOT_RESOURCE_PACKAGE = "com.enteks.atmosphere.samples.resources.jersey";
    private final static String ROOT_URL = "/";
    private final static Logger logger = LoggerFactory.getLogger(JerseyResourceTest.class);

    public JerseyResourceTest() throws Exception {
        super(JERSEY_ROOT_RESOURCE_PACKAGE);
    }

    @Test
    public void testDoGet() throws Exception {
        String expectedResponse = "This is the plain Jersey GET method.\n";
        String response = webResource.path(ROOT_URL+"get").get(String.class);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDoPost() throws Exception {
        String expectedResponse = "You posted this: unitTestMessage\n";
        Form formData = new Form();
        formData.add("message", "unitTestMessage");
        ClientResponse response = webResource.path(ROOT_URL+"post").type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(ClientResponse.class, formData);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // check that the generated reponse is the expected one
        InputStream responseInputStream = response.getEntityInputStream();
        try {
            byte[] responseData = new byte[responseInputStream.available()];
            responseInputStream.read(responseData);
            assertTrue(new String(responseData).contains(expectedResponse));
        } catch (IOException ex) {
            logger.error("IOException in testDoPost()", ex);
        }
    }
}
