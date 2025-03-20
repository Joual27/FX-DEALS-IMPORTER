package com.bloomberg.fx_deals_importer.config;


import com.bloomberg.fx_deals_importer.exceptions.BatchProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BatchProcessingException.class)
    @ResponseStatus(HttpStatus.MULTI_STATUS)
    public ResponseEntity<Map<String, Object>> handleBatchProcessingException(BatchProcessingException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("successfulDeals", ex.getSuccesses());
        response.put("errors", ex.getErrors());

        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
    }
}
