package com.bloomberg.fx_deals_importer.mappers;

import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.entities.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Currency;

@Mapper(componentModel = "spring")
public interface DealMapper {
    @Mapping(target = "fromCurrency", expression = "java(toCurrency(dto.fromCurrency()))")
    @Mapping(target = "toCurrency", expression = "java(toCurrency(dto.toCurrency()))")
    Deal toEntity(CreateDealDTO dto);
    DealResponseDTO toResponseDTO(Deal entity);

    default Currency toCurrency(String code) {
        return Currency.getInstance(code);
    }
}
