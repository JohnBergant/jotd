package com.example.jotd.domain.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Authentication converter for API authentication.
 */
public class ApiAuthenticationConverter implements AuthenticationConverter {
    private final static String API_KEY_HEADER_NAME = "X-API-KEY";

    @Override
    public Authentication convert(HttpServletRequest request) {
        final String apiKey = request.getHeader(API_KEY_HEADER_NAME);
        if (apiKey != null) {
            return new PreAuthenticatedAuthenticationToken(new ApiPrincipal(), apiKey);
        }

        return null;
    }
}
