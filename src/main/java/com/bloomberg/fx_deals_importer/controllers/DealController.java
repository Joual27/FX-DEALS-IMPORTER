package com.bloomberg.fx_deals_importer.controllers;

import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.helpers.DTOs.SuccessDTO;
import com.bloomberg.fx_deals_importer.services.interfaces.DealService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<SuccessDTO<List<DealResponseDTO>>> importDeals(@RequestBody List<CreateDealDTO> req){
        List<DealResponseDTO> res = dealService.createDeals(req);
        return new ResponseEntity<>(new SuccessDTO<>("success" , "deals created successfully !" , LocalDateTime.now() , res ) , HttpStatus.OK);
    }
}
