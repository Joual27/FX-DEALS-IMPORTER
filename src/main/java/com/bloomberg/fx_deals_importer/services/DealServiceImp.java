package com.bloomberg.fx_deals_importer.services;

import com.bloomberg.fx_deals_importer.DAOs.DealDAO;
import com.bloomberg.fx_deals_importer.DTOs.CreateDealDTO;
import com.bloomberg.fx_deals_importer.DTOs.DealResponseDTO;
import com.bloomberg.fx_deals_importer.entities.Deal;
import com.bloomberg.fx_deals_importer.exceptions.AlreadyExistsException;
import com.bloomberg.fx_deals_importer.exceptions.BatchProcessingException;
import com.bloomberg.fx_deals_importer.exceptions.CurrencyException;
import com.bloomberg.fx_deals_importer.mappers.DealMapper;
import com.bloomberg.fx_deals_importer.services.interfaces.DealService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@AllArgsConstructor
@Service
public class DealServiceImp implements DealService {
    private static final Logger logger = LoggerFactory.getLogger(DealServiceImp.class);
    private final DealDAO dealDAO;
    private final DealMapper dealMapper;

    // In this method , Since it is mentioned that no rollbacks are allowed , It means it is expected to get an Array of deals . If we use the regular
    // approach and handle the AlreadyExistsException | CurrencyException | IllegalArgumentException in the Global exception handler , When it will face
    // the first exception it will immediately return the configured response by the exception handler (so we can't know which deals were added successfully and which ones didnt !).
    @Override
    public List<DealResponseDTO> createDeals(List<CreateDealDTO> data) {
        List<DealResponseDTO> successes = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        logger.info("Starting To Process {} Deals", data.size());
        for (CreateDealDTO dto : data) {
            try {
                validateRequestDTO(dto);
                validateCurrencyCodes(dto.id(), dto.fromCurrency(), dto.toCurrency());
                checkForIdUniqueness(dto.id());
                Deal deal = dealMapper.toEntity(dto);
                deal.setMadeAt(LocalDateTime.now());
                successes.add(dealMapper.toResponseDTO(dealDAO.save(deal)));
                logger.info("Deal {} Imported Succesfully !: {}", dto.id(), responseDTO);
            }catch (ValidationException e){
                logger.error("Validation Errors for deal {} : {}", dto.id() , e.getMessage());
                errors.add(e.getMessage());
            }
            catch (AlreadyExistsException | CurrencyException | IllegalArgumentException e){
                logger.error("Error processing deal {} : {}", dto.id() , e.getMessage());
                errors.add(e.getMessage());
            }
        }
        if (!errors.isEmpty()){
            throw new BatchProcessingException("some deals couldn't be processed !" , successes , errors);
            logger.info(" {} Deals Were Imported Successfully" , data.size() - errors.size());
        }
        return successes;
    }

    private void validateCurrencyCodes(String dealId, String fromCurrency, String toCurrency) {
        try {
            Currency from = Currency.getInstance(fromCurrency);
            Currency to = Currency.getInstance(toCurrency);
            if (from.equals(to)) {
                throw new CurrencyException("Deal " + dealId + ": Currencies must be different!");
            }
        } catch (IllegalArgumentException e) {
            throw new CurrencyException("Deal " + dealId + ": Invalid ISO currency code - " + e.getMessage());
        }
    }

    private void checkForIdUniqueness(String id) {
        if (dealDAO.existsById(id)) {
            throw new AlreadyExistsException("Deal " + id + ": There's already an existing deal with this ID!");
        }
    }


    //here im forced to write my own validation method ! Because if I use validation annotations ! and @Valid in the controller , if the request has one
    // or more invalid deals , The response will be returned from the controller level , and so we can't handle each deal separately as we are willing to do
    private void validateRequestDTO(CreateDealDTO dto) {
        List<String> errors = new ArrayList<>();
        String idDisplay = (dto.id() != null) ? dto.id() : "[ID: null]";

        if (dto.id() == null) errors.add("ID is required");
        if (dto.fromCurrency() == null) errors.add("From currency is required");
        if (dto.toCurrency() == null) errors.add("To currency is required");
        if (dto.amount() <= 0) errors.add("Amount must be positive");

        if (!errors.isEmpty()) {
            throw new ValidationException("Deal " + idDisplay + ": " + String.join(", ", errors));
        }
    }
}
