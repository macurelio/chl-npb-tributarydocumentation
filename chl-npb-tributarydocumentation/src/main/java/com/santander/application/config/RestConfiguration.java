package com.santander.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestBindingMode;

@Component
@Configuration
public class RestConfiguration extends RouteBuilder {

    @Value("${beans.server.host}")
    private String host;

    @Value("${beans.server.port}")
    private String port;

    @Value("${beans.server.chunked-max-content-length}")
    private Integer chunkedSize;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("netty-http")
                .componentProperty("chunkedMaxContentLength", String.valueOf(chunkedSize * 1024 * 1024))
                .endpointProperty("chunkedMaxContentLength", String.valueOf(chunkedSize * 1024 * 1024))
                .consumerProperty("chunkedMaxContentLength", String.valueOf(chunkedSize * 1024 * 1024))
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/")
                .host(host)
                .port(port)
                // add OpenApi api-doc out of the box
                .apiContextPath("/v3/api-docs")
                    .apiProperty("api.title", "API Definition of " + "chl-npb-tributarydocumentation".toUpperCase())
                    .apiProperty("api.version", "1.0.0")
                    // and enable CORS
                    .apiProperty("cors", "true");
    }

}   