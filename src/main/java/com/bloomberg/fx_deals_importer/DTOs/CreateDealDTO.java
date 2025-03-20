package com.bloomberg.fx_deals_importer.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Currency;

public record CreateDealDTO(@NotNull String id , @NotNull Currency fromCurrency , @NotNull Currency toCurrency , @NotNull @Min(1) double amount ) {
}
