package com.bloomberg.fx_deals_importer.mappers;

import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.entities.Deal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealMapper {
    Deal toEntity(CreateDealDTO dto);
    DealResponseDTO toResponseDTO(Deal deal);
}
