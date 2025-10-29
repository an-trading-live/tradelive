package com.antrade.tradelive.repository;

import com.antrade.tradelive.entity.na_auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaAuthRepository extends JpaRepository<na_auth, Long> {
    Optional<na_auth> findByEmail(String email);
    Optional<na_auth> findByMobileNumber(String mobileNumber);
    Optional<na_auth> findByUniqueId(String uniqueId);
}