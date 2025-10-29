package com.antrade.tradelive.service;

import com.antrade.tradelive.entity.na_auth;
import com.antrade.tradelive.repository.NaAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NaAuthService {

    @Autowired
    private NaAuthRepository naAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<na_auth> getAllUsers() {
        return naAuthRepository.findAll();
    }

    public Optional<na_auth> getUserById(Long id) {
        return naAuthRepository.findById(id);
    }

    public Optional<na_auth> getUserByEmail(String email) {
        return naAuthRepository.findByEmail(email);
    }

    public Optional<na_auth> getUserByMobileNumber(String mobileNumber) {
        return naAuthRepository.findByMobileNumber(mobileNumber);
    }

    public Optional<na_auth> getUserByUniqueId(String uniqueId) {
        return naAuthRepository.findByUniqueId(uniqueId);
    }

    public na_auth createUser(na_auth user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getMobileNumber() == null) {
            throw new IllegalArgumentException("Email, password, and mobile number are required");
        }
        if (getUserByEmail(user.getEmail()).isPresent() || getUserByMobileNumber(user.getMobileNumber()).isPresent()) {
            throw new RuntimeException("User with this email or mobile already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return naAuthRepository.save(user);
    }

    public na_auth updateUser(Long id, na_auth userDetails) {
        Optional<na_auth> optionalUser = naAuthRepository.findById(id);
        if (optionalUser.isPresent()) {
            na_auth user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            user.setOtp(userDetails.getOtp());
            user.setUniqueId(userDetails.getUniqueId());
            user.setMobileNumber(userDetails.getMobileNumber());
            return naAuthRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public void deleteUser(Long id) {
        Optional<na_auth> optionalUser = naAuthRepository.findById(id);
        if (optionalUser.isPresent()) {
            naAuthRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public Optional<na_auth> authenticateUser(String identifier, String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return Optional.empty();
        }
        Optional<na_auth> userOpt = Optional.ofNullable(getUserByEmail(identifier).orElse(null))
                .or(() -> Optional.ofNullable(getUserByMobileNumber(identifier).orElse(null)));
        if (userOpt.isPresent()) {
            na_auth user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}