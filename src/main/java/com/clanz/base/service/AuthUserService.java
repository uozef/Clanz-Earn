package com.clanz.base.service;

import com.clanz.base.domain.dto.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    @Lazy
    @Autowired
    private AuthenticationRetriever<UsernamePasswordAuthenticationToken, AuthUser> authenticationRetriever;

    @PreAuthorize("isAuthenticated()")
    public AuthUser getAuthUser() {
        return authenticationRetriever.getPrincipal();
    }
}
