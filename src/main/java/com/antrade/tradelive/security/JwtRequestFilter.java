package com.antrade.tradelive.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws jakarta.servlet.ServletException, java.io.IOException {  // Fully qualified to force Jakarta + java.io resolution
        final String authorizationHeader = request.getHeader("na-auth-token");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(jwt);
                } catch (ExpiredJwtException e) {
                    logger.warn("Token expired for request: {}", request.getRequestURI());
                    handleJwtError(response, "Token has expired");
                    return;
                } catch (SignatureException e) {
                    logger.warn("Invalid token signature for request: {}", request.getRequestURI());
                    handleJwtError(response, "Invalid token signature");
                    return;
                } catch (JwtException e) {
                    logger.warn("Invalid JWT token for request: {}", request.getRequestURI(), e);
                    handleJwtError(response, "Invalid token");
                    return;
                }
            } else {
                logger.warn("Invalid token format in header for request: {}", request.getRequestURI());
                handleJwtError(response, "Invalid token format - use 'Bearer <token>'");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    chain.doFilter(request, response);  // Direct call now resolves correctly
                    return;  // Explicit return after successful auth
                } else {
                    logger.warn("Token validation failed for user: {}", username);
                    handleJwtError(response, "Invalid token");
                    return;
                }
            } catch (JwtException e) {
                logger.warn("Token validation exception for request: {}", request.getRequestURI(), e);
                handleJwtError(response, "Invalid token");
                return;
            } catch (Exception e) {
                logger.error("Unexpected error in token validation", e);
                handleJwtError(response, "Authentication error: " + e.getMessage());
                return;
            }
        } else if (authorizationHeader == null) {
            // No token provided - log and let Spring Security handle if required
            logger.debug("No token provided for request: {}", request.getRequestURI());
        }
try {
        chain.doFilter(request, response); 
    } catch (JwtException e) {
        logger.warn("Token validation exception for request: {}", request.getRequestURI(), e);
        handleJwtError(response, "Invalid token");
        return;
    } catch (Exception e) {
        logger.error("Unexpected error in token validation", e);
        handleJwtError(response, "Authentication error: " + e.getMessage());
        return;
    }// Direct call now resolves correctly
    }

    private void handleJwtError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"error\": \"" + message + "\"}";
        response.getWriter().print(jsonResponse);
        response.getWriter().flush();
        response.flushBuffer();
    }
}