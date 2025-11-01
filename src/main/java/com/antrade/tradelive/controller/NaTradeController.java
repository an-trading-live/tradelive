package com.antrade.tradelive.controller;

import com.angelbroking.smartapi.BuyandSellSmartApi;
import com.angelbroking.smartapi.MainIncluded;
import com.antrade.tradelive.entity.na_trade;
import com.antrade.tradelive.service.NaTradeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "*")  // Optional: Enable CORS for frontend
public class NaTradeController {

    @Autowired
    private NaTradeService naTradeService;

    // GET all trades
    @GetMapping
    public ResponseEntity<List<na_trade>> getAllTrades() {
        List<na_trade> trades = naTradeService.getAllTrades();
        return ResponseEntity.ok(trades);
    }
    // testing 
    // GET trade by ID
    @GetMapping("/{id}")
    public ResponseEntity<na_trade> getTradeById(@PathVariable Long id) {
        Optional<na_trade> trade = naTradeService.getTradeById(id);
        return trade.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // GET trades by date (e.g., /api/trades/date/2025-10-24)
    @GetMapping("/date/{date}")
    public ResponseEntity<List<na_trade>> getTradesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<na_trade> trades = naTradeService.getTradesByDate(date);
        return ResponseEntity.ok(trades);
    }

    // GET trades by date range (e.g., /api/trades/range?startDate=2025-10-01&endDate=2025-10-31)
    @GetMapping("/range")
    public ResponseEntity<List<na_trade>> getTradesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<na_trade> trades = naTradeService.getTradesByDateRange(startDate, endDate);
        return ResponseEntity.ok(trades);
    }

    // GET high-entry trades by date (e.g., /api/trades/high-entry?date=2025-10-24&minPrice=100.00)
    @GetMapping("/high-entry")
    public ResponseEntity<List<na_trade>> getHighEntryTradesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam BigDecimal minPrice) {
        List<na_trade> trades = naTradeService.getHighEntryTradesByDate(date, minPrice);
        return ResponseEntity.ok(trades);
    }

    // GET profitable trades
//    @GetMapping("/profitable")
//    public ResponseEntity<List<na_trade>> getProfitableTrades() {
//        List<na_trade> trades = naTradeService.getProfitableTrades();
//        return ResponseEntity.ok(trades);
//    }

    // POST create new trade
    @PostMapping
    public ResponseEntity<na_trade> createTrade(@Valid @RequestBody na_trade trade) {
        na_trade createdTrade = naTradeService.createTrade(trade);
        return ResponseEntity.ok(createdTrade);
    }

    // PUT update trade by ID
    @PutMapping("/{id}")
    public ResponseEntity<na_trade> updateTrade(@PathVariable Long id, @Valid @RequestBody na_trade tradeDetails) {
        try {
            na_trade updatedTrade = naTradeService.updateTrade(id, tradeDetails);
            return ResponseEntity.ok(updatedTrade);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    

    // DELETE trade by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long id) {
        try {
            naTradeService.deleteTrade(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // new incluing this JAR
    
    @PostMapping("/userConfig")
    public ResponseEntity<String> initiateTradingJson(@Valid @RequestBody Map<String, Object> requestBody)  {
        try {
            // Manual extraction & validation (less safe than DTO)
            String apiKey = (String) requestBody.get("apiKey");
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("API Key is required");
            }
            
            String dayExpiry = ((String) requestBody.get("dayExpiry"));
            if (dayExpiry == null || dayExpiry.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Day Expiry must be a positive number");
            }
            
            String totp_secret = (String) requestBody.get("totp_key");
            if (totp_secret == null || totp_secret.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("totp Key Code is required");
            }
            String client_id = (String) requestBody.get("clientId");
            if (client_id == null || client_id.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("client_id Key is required");
            }
            
            String app_lock_key = (String) requestBody.get("app_lock_key");
            if (app_lock_key == null || app_lock_key.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("app_lock_key Key is required");
            }
            
            Double capitalAmount = ((Number) requestBody.get("capitalAmount")).doubleValue();
            if (capitalAmount == null || capitalAmount <= 0) {
                return ResponseEntity.badRequest().body("Capital Amount must be a positive number");
            }
            String test_env = ((String) requestBody.get("test"));
            if (test_env == null || test_env.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("test need to provide true");
            }
            String custom_date = (String) requestBody.get("custom_date");
            if (custom_date == null || custom_date.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("custom_date Key is 2025-10-23T10:15:00+05:30");
            }
            Integer no_candle = ((Number) requestBody.get("test_size")).intValue();
            if (no_candle == null || no_candle <= 0) {
                return ResponseEntity.badRequest().body("test_size must be a positive number");
            }
            
            String indices = (String) requestBody.get("indices");
            if (custom_date == null || custom_date.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("empty indices Key indices");
            }
            BuyandSellSmartApi buyandSellSmartApi = new BuyandSellSmartApi();
			//            BuyandSellSmartApi buy_sell = new BuyandSellSmartApi();
//            return ResponseEntity.ok("Trading initiated with capital: " + capitalAmount);
//            buy
//            this.API_KEY = API_KEY;
//    		this.CLIENT_ID = CLIENT_ID;
//    		BuyandSellSmartApi.lock_app = lock_app;
//    		BuyandSellSmartApi.TOTP_SECRET = TOTP_SECRET;
//    		BuyandSellSmartApi.expiryDay = expiryDay;
//    		this.capitalAmount = capitalAmount;
//            buyandSellSmartApi.setvalues(apiKey, client_id, app_lock_key, totp_secret, dayExpiry, capitalAmount);
            boolean test = false;
            if(test_env == "true") {
            	test = true;
            }
            try {
            	int exp_date = Integer.parseInt(dayExpiry);
//            	boolean test,String custom_date,int no_candle
            	MainIncluded.StartTrading(apiKey, client_id, app_lock_key, totp_secret, exp_date, capitalAmount,test,custom_date,no_candle,indices);      	
//            	MainIncluded.StartTrading();
            }catch( Exception e) {
            	return ResponseEntity.ok(e+"");
            }
            return ResponseEntity.ok("Trading initiated with capital: " + capitalAmount);
        } catch (Throwable e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }
}