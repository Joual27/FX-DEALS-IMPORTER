package com.bloomberg.fx_deals_importer.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Deal {
    @Id
    private String id ;
    private Currency fromCurrency ;
    private Currency toCurrency;
    private LocalDateTime dealTimeStamp ;
    private double amount ;
}
