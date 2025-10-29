package com.antrade.tradelive.security;

import com.antrade.tradelive.entity.na_auth;
import com.antrade.tradelive.repository.NaAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private NaAuthRepository naAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrMobile) throws UsernameNotFoundException {
        na_auth user = naAuthRepository.findByEmail(emailOrMobile).orElse(
                naAuthRepository.findByMobileNumber(emailOrMobile).orElse(null));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email or mobile: " + emailOrMobile);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}