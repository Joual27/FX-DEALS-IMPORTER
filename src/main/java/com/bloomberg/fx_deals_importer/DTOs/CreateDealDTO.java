package com.bloomberg.fx_deals_importer.DTOs;


import java.util.Currency;

public record CreateDealDTO( String id , String fromCurrency ,  String toCurrency , double amount ) {
}
