package com.clanz.account.service;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.clanz.base.config.AwsProperties;
import com.clanz.base.config.CognitoProperties;
import com.clanz.base.exception.ClanzException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
@RequiredArgsConstructor
public class CognitoJwtService implements RSAKeyProvider {
    private final AwsProperties awsProperties;
    private final CognitoProperties cognitoProperties;
    private JwkProvider jwkProvider;

    @PostConstruct
    private void init() {
        try {
            final var awsRegion = awsProperties.getRegion();
            final var cognitoPoolId = cognitoProperties.getPoolId();
            final var url = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", awsRegion, cognitoPoolId);
            jwkProvider = new JwkProviderBuilder(new URL(url)).build();
        } catch (Exception e) {
            throw new ClanzException(e, HttpStatus.INTERNAL_SERVER_ERROR, "can not load aws jwk");
        }
    }

    public String getUrl() {
        final var awsRegion = awsProperties.getRegion();
        final var cognitoPoolId = cognitoProperties.getPoolId();
        return String.format("https://cognito-idp.%s.amazonaws.com/%s", awsRegion, cognitoPoolId);
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try {
            return (RSAPublicKey) jwkProvider.get(kid).getPublicKey();
        } catch (Exception e) {
            throw new ClanzException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get JWT");
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
