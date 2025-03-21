package com.bloomberg.fx_deals_importer.DTOs;

public record CreateDealDTO( String id , String fromCurrency ,  String toCurrency , double amount ) {
}
