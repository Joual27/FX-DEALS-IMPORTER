package com.bloomberg.fx_deals_importer.services;

import com.bloomberg.fx_deals_importer.DAOs.DealDAO;
import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.exceptions.CurrencyException;
import com.bloomberg.fx_deals_importer.mappers.DealMapper;
import com.bloomberg.fx_deals_importer.services.interfaces.DealService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;

@AllArgsConstructor
@Service
public class DealServiceImp implements DealService {
    private final DealDAO dealDAO;
    private final DealMapper dealMapper;

    @Override
    public List<DealResponseDTO> createDeals(List<CreateDealDTO> data) {
        return List.of();
    }

    private void validateCurrencyCodes(Currency fromCurrency , Currency toCurrency){
        try {
            Currency from = Currency.getInstance(fromCurrency.getCurrencyCode());
            Currency to = Currency.getInstance(toCurrency.getCurrencyCode());
            if (from.equals(to)){
                throw new CurrencyException("Deal Currencies must be different !");
            }
        }catch (IllegalArgumentException e){
            throw new CurrencyException("Invalid Currency ISO : " + e.getMessage());
        }
    }
}
