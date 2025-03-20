package com.bloomberg.fx_deals_importer.services.interfaces;

import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;

import java.util.List;

public interface DealService {
    List<DealResponseDTO> createDeals(List<CreateDealDTO> data);
}
