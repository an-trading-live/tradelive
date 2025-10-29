package com.antrade.tradelive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity  // Maps to a table
@Table(name = "na_trade")  // Specify table name
public class na_trade {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID
    private Long id;

    @NotNull(message = "Trade date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank(message = "Exchange is required")
    @Column(nullable = false, length = 10)  // e.g., "NSE", "BSE"
    private String exchangeString;

    @NotNull(message = "Entry price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Entry price must be positive")
    @Column(nullable = false, precision = 10, scale = 2)  // e.g., 100.50
    private BigDecimal entryPrice;

    @NotNull(message = "Exit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exit price must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal exitPrice;

    @NotNull(message = "Entry time is required")
    @Column(nullable = false)
    private LocalTime timeOfEntry;

    @NotNull(message = "Exit time is required")
    @Column(nullable = false)
    private LocalTime timeOfExit;

    // Constructors
    public na_trade() {}

    public na_trade(LocalDate date, String exchangeString, BigDecimal entryPrice, BigDecimal exitPrice,
                 LocalTime timeOfEntry, LocalTime timeOfExit) {
        this.date = date;
        this.exchangeString = exchangeString;
        this.entryPrice = entryPrice;
        this.exitPrice = exitPrice;
        this.timeOfEntry = timeOfEntry;
        this.timeOfExit = timeOfExit;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getExchangeString() { return exchangeString; }
    public void setExchangeString(String exchangeString) { this.exchangeString = exchangeString; }

    public BigDecimal getEntryPrice() { return entryPrice; }
    public void setEntryPrice(BigDecimal entryPrice) { this.entryPrice = entryPrice; }

    public BigDecimal getExitPrice() { return exitPrice; }
    public void setExitPrice(BigDecimal exitPrice) { this.exitPrice = exitPrice; }

    public LocalTime getTimeOfEntry() { return timeOfEntry; }
    public void setTimeOfEntry(LocalTime timeOfEntry) { this.timeOfEntry = timeOfEntry; }

    public LocalTime getTimeOfExit() { return timeOfExit; }
    public void setTimeOfExit(LocalTime timeOfExit) { this.timeOfExit = timeOfExit; }
}