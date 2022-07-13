package com.clanz.base.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationRetriever<A extends Authentication, P> {
    private final Class<A> authenticationClass;
    private final Class<P> principalClass;

    public AuthenticationRetriever(final Class<A> authenticationClass, final Class<P> principalClass) {
        this.authenticationClass = authenticationClass;
        this.principalClass = principalClass;
    }

    public A getAuthentication() {
        try {
            final var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authenticationClass.cast(SecurityContextHolder.getContext().getAuthentication());
            } else {
                return null;
            }
        } catch (ClassCastException e) {
            return null;
        }
    }

    public P getPrincipal() {
        final var authentication = getAuthentication();
        return authentication == null ? null : principalClass.cast(authentication.getPrincipal());
    }
}