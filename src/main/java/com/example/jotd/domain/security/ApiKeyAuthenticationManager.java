package com.example.jotd.domain.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Authentication manager for API keys.
 */
@Service
public class ApiKeyAuthenticationManager implements AuthenticationManager {

    @Value("${jotd.api.key}")
    String apiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() != null && apiKey.equals(authentication.getCredentials().toString())) {
            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_USER"));

            return new PreAuthenticatedAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authorities);
        }
        return null;
    }
}
