package com.azure.cosmosdb.demo;

import com.azure.cosmos.CosmosException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Cosmos related errors will get converted to 500 by spring (e.g. rate-limiting
// 429). this is
// handled explicitly
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CosmosExceptionHandler {

    @ExceptionHandler(CosmosException.class)
    public ResponseEntity<String> handleCosmosException(CosmosException e) {
        return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).body(e.getMessage());
    }
}
