package com.example.jotd.domain.security;

import java.security.Principal;

public class ApiPrincipal implements Principal {
    @Override
    public String getName() {
        return "Trusted API Key";
    }
}
