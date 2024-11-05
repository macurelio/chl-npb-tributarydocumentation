package com.santander.application.beans;

import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;

import com.santander.application.model.exception.LevelEnum;
import com.santander.application.model.exception.ServiceException;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class RestServiceValidations {

    private static final String MSG_BAD_REQUEST = "The request could not be processed, please try again later.";

    public void  bis57Validation(Exchange __exchange) 
        throws ServiceException{

        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        Map<String, Object> bis57Data = (Map<String, Object>) flows.get("57bis-data");
        Map<String, Object> response = (Map<String, Object>) bis57Data.get("response");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        

        if (!"200".equalsIgnoreCase(headers.get("HttpStatusCodeResponse").toString())) {
			// log.error("Error respuesta B2D {}", bodyResponse);
			throw new ServiceException(HttpStatus.BAD_REQUEST.value(), 409, MSG_BAD_REQUEST, LevelEnum.ERROR, "Error consuming data source");
		}

        if ("200".equalsIgnoreCase(headers.get("HttpStatusCodeResponse").toString()) && body.isEmpty()) {
			// log.error("Error respuesta B2D {}", bodyResponse);
			throw new ServiceException(HttpStatus.BAD_REQUEST.value(), 404, MSG_BAD_REQUEST, LevelEnum.ERROR, "Data not found");
		}
    }
}
