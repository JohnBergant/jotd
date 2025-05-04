package com.example.jotd.domain.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that checks for the presence of an X-API-KEY and authentications the request
 */
@AllArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiAuthenticationConverter authenticationConverter;

    private final ApiKeyAuthenticationManager apiKeyAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = authenticationConverter.convert(request);
            if (authentication == null) {
                this.logger.trace("Did not process authentication request since failed to find "
                        + "username and password in Basic Authorization header");
                filterChain.doFilter(request, response);
                return;
            }
            Authentication auth = apiKeyAuthenticationManager.authenticate(authentication);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new SecurityException("Authentication failed");
            }
        } catch (Exception ex) {
            this.logger.trace("Did not process authentication request since failed to authenticate", ex);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/v1/jokes/daily");
    }
}
