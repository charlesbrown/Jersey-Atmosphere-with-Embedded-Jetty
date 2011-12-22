This is a sample implementation of a simple, RESTful web service using Atmosphere and Jersey, running as an executable JAR file with embedded Jetty server.

Atmosphere Project -- https://github.com/Atmosphere/atmosphere

Jersey -- http://jersey.java.net/

Jetty -- http://www.eclipse.org/jetty/

The sample uses a single instance of an embedded Jetty server to configure two servlets, one for a simple REST implementation of Jersey, and the other, a combination of Jersey and Atmosphere to support WebSocket/Comet framework.