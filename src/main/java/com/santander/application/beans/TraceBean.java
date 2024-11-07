package com.santander.application.beans;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceBean {

    private ObjectMapper mapper = new ObjectMapper();

    public void interceptor(Exchange exchange){
        var body = exchange.getIn().getBody();
        var headers = exchange.getIn().getHeaders();
        var properties = exchange.getProperties();
        try {
            log.trace("{} {} {} {} {}", exchange.getFromRouteId(), exchange.getFromEndpoint(), mapper.writeValueAsString(headers), mapper.writeValueAsString(properties), body);
        } catch (JsonProcessingException e) {
            log.error("Cannot parse to json on TraceBean");
        }
    }
    
}
