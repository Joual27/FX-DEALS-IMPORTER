package com.bloomberg.fx_deals_importer.exceptions;

import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class BatchProcessingException extends RuntimeException {
    private final List<DealResponseDTO> successes;
    private final List<String> errors;
    public BatchProcessingException(String message, List<DealResponseDTO> successes, List<String> errors) {
        super(message);
        this.successes = successes;
        this.errors = errors;
    }
}
