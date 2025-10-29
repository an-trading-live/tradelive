package com.antrade.tradelive.service;

import com.antrade.tradelive.entity.na_trade;
import com.antrade.tradelive.repository.NaTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NaTradeService {

    @Autowired
    private NaTradeRepository naTradeRepository;

    // Retrieve all trades
    public List<na_trade> getAllTrades() {
        return naTradeRepository.findAll();
    }

    // Retrieve by ID
    public Optional<na_trade> getTradeById(Long id) {
        return naTradeRepository.findById(id);
    }

    // Retrieve trades by date
    public List<na_trade> getTradesByDate(LocalDate date) {
        return naTradeRepository.findByDate(date);
    }

    // Retrieve trades by date range
    public List<na_trade> getTradesByDateRange(LocalDate startDate, LocalDate endDate) {
        return naTradeRepository.findByDateBetween(startDate, endDate);
    }

    // Retrieve high-entry trades by date (custom query)
    public List<na_trade> getHighEntryTradesByDate(LocalDate date, BigDecimal minPrice) {
        return naTradeRepository.findHighEntryTradesByDate(date, minPrice);
    }

    // Retrieve trades by date using native query (if needed)
    public List<na_trade> getTradesByDateNative(LocalDate date) {
        return naTradeRepository.findByDateNative(date);
    }

    // Create a new trade (insert)
    public na_trade createTrade(na_trade trade) {
        return naTradeRepository.save(trade);
    }


    // Update an existing trade
    public na_trade updateTrade(Long id, na_trade tradeDetails) {
        Optional<na_trade> optionalTrade = naTradeRepository.findById(id);
        if (optionalTrade.isPresent()) {
            na_trade trade = optionalTrade.get();
            trade.setDate(tradeDetails.getDate());
            trade.setExchangeString(tradeDetails.getExchangeString());
            trade.setEntryPrice(tradeDetails.getEntryPrice());
            trade.setExitPrice(tradeDetails.getExitPrice());
            trade.setTimeOfEntry(tradeDetails.getTimeOfEntry());
            trade.setTimeOfExit(tradeDetails.getTimeOfExit());
            return naTradeRepository.save(trade);
        } else {
            throw new RuntimeException("Trade not found with id " + id);
        }
    }

    // Delete a trade
    public void deleteTrade(Long id) {
        Optional<na_trade> optionalTrade = naTradeRepository.findById(id);
        if (optionalTrade.isPresent()) {
            naTradeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Trade not found with id " + id);
        }
    }
}