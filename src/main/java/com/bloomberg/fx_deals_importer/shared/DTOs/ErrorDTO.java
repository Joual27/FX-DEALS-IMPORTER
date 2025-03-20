package com.bloomberg.fx_deals_importer.shared.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> validationErrors;
    private LocalDateTime timestamp;

    public ErrorDTO(int statusCode,String message,LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }
    public ErrorDTO(int statusCode,String message, Map<String, String> validationErrors , LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.validationErrors = validationErrors;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Map<String , String> getValidationErrors(){
        return validationErrors;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }

}