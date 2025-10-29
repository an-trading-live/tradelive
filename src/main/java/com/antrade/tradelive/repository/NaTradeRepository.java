package com.antrade.tradelive.repository;

import com.antrade.tradelive.entity.na_trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NaTradeRepository extends JpaRepository<na_trade, Long> {

    // Auto-generated: SELECT * FROM na_trade WHERE date = ?
    List<na_trade> findByDate(LocalDate date);

    // For date range: SELECT * FROM na_trade WHERE date BETWEEN :start AND :end
    List<na_trade> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Custom JPQL for specific example (e.g., trades on a date with entryPrice > 100)
    @Query("SELECT t FROM na_trade t WHERE t.date = :date AND t.entryPrice > :price")
    List<na_trade> findHighEntryTradesByDate(@Param("date") LocalDate date, @Param("price") BigDecimal price);

    // NEW: Profitable trades (exitPrice > entryPrice)
    @Query("SELECT t FROM na_trade t WHERE t.exitPrice > t.entryPrice")
    List<na_trade> findProfitableTrades();

    // Native SQL example (if needed for complex joins)
    @Query(value = "SELECT * FROM na_trade WHERE date = :date", nativeQuery = true)
    List<na_trade> findByDateNative(@Param("date") LocalDate date);
}