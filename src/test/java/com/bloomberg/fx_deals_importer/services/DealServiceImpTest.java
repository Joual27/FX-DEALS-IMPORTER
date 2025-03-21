package com.bloomberg.fx_deals_importer.services;

import com.bloomberg.fx_deals_importer.DAOs.DealDAO;
import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.entities.Deal;
import com.bloomberg.fx_deals_importer.exceptions.BatchProcessingException;
import com.bloomberg.fx_deals_importer.mappers.DealMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceImpTest {

    @Mock
    private DealDAO dealDAO;

    @Mock
    private DealMapper dealMapper;

    @InjectMocks
    private DealServiceImp dealService;

    private CreateDealDTO validDeal;
    private DealResponseDTO validDealResponse;

    @BeforeEach
    void setUp() {
        validDeal = new CreateDealDTO("D001", "USD", "EUR", 100.0);
        validDealResponse = new DealResponseDTO("D001", Currency.getInstance("USD"), Currency.getInstance("EUR"), 100.0, LocalDateTime.now());
    }

    @Test
    void testCreateDeals_ValidDeal() {
        when(dealDAO.existsById("D001")).thenReturn(false);
        when(dealMapper.toEntity(validDeal)).thenReturn(new Deal());
        when(dealDAO.save(any(Deal.class))).thenReturn(new Deal());
        when(dealMapper.toResponseDTO(any(Deal.class))).thenReturn(validDealResponse);

        List<DealResponseDTO> result = dealService.createDeals(List.of(validDeal));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("D001", result.get(0).id());
    }

    @Test
    void testCreateDeals_MissingId() {
        CreateDealDTO invalidDeal = new CreateDealDTO(null, "USD", "EUR", 100.0);

        BatchProcessingException exception = assertThrows(BatchProcessingException.class, () -> {
            dealService.createDeals(List.of(invalidDeal));
        });

        assertTrue(exception.getErrors().get(0).contains("ID is required"));
    }

    @Test
    void testCreateDeals_InvalidCurrencyCode() {
        CreateDealDTO invalidDeal = new CreateDealDTO("D002", "INVALID", "EUR", 100.0);

        BatchProcessingException exception = assertThrows(BatchProcessingException.class, () -> {
            dealService.createDeals(List.of(invalidDeal));
        });

        assertTrue(exception.getErrors().get(0).contains("Invalid ISO currency code"));
    }

    @Test
    void testCreateDeals_SameCurrencies() {
        CreateDealDTO invalidDeal = new CreateDealDTO("D003", "USD", "USD", 100.0);

        BatchProcessingException exception = assertThrows(BatchProcessingException.class, () -> {
            dealService.createDeals(List.of(invalidDeal));
        });

        assertTrue(exception.getErrors().get(0).contains("Currencies must be different"));
    }


    @Test
    void testCreateDeals_DuplicateId() {
        when(dealDAO.existsById("D005")).thenReturn(true);

        CreateDealDTO invalidDeal = new CreateDealDTO("D005", "USD", "EUR", 100.0);

        BatchProcessingException exception = assertThrows(BatchProcessingException.class, () -> {
            dealService.createDeals(List.of(invalidDeal));
        });

        assertTrue(exception.getErrors().get(0).contains("There's already an existing deal with this ID"));
    }

    @Test
    void testCreateDeals_PartialSuccess() {
        CreateDealDTO validDeal = new CreateDealDTO("D006", "USD", "EUR", 100.0);
        CreateDealDTO invalidDeal = new CreateDealDTO("D007", "USD", "USD", 200.0);

        when(dealDAO.existsById("D006")).thenReturn(false);
        when(dealMapper.toEntity(validDeal)).thenReturn(new Deal());
        when(dealDAO.save(any(Deal.class))).thenReturn(new Deal());
        when(dealMapper.toResponseDTO(any(Deal.class))).thenReturn(validDealResponse);

        BatchProcessingException exception = assertThrows(BatchProcessingException.class, () -> {
            dealService.createDeals(List.of(validDeal, invalidDeal));
        });
        assertEquals(1, exception.getSuccesses().size());
        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getErrors().get(0).contains("Currencies must be different"));
    }

    @Test
    void testCreateDeals_EmptyList() {
        List<DealResponseDTO> result = dealService.createDeals(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}