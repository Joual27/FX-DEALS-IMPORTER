package com.bloomberg.fx_deals_importer.DTOs;

import java.util.Currency;

public record ImportDealDTO(String id , Currency fromCurrency , Currency toCurrency , double amount ) {
}
