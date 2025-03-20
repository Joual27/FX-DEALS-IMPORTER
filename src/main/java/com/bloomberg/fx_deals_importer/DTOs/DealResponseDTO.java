package com.bloomberg.fx_deals_importer.DTOs;

import java.time.LocalDateTime;
import java.util.Currency;

public record DealResponseDTO(String id , Currency fromCurrency , Currency toCurrency , double amount , LocalDateTime madeAt) {
}
