package com.clanz.base.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.clanz.account.service.CognitoJwtService;
import com.clanz.account.service.CognitoService;
import com.clanz.base.domain.dto.AuthUser;
import com.clanz.base.exception.ClanzException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthenticationTokenService {
    private final CognitoJwtService cognitoJwtService;

    public AuthUser getAuthUserFromAuthorizationToken(String token) {
        try {
            token = token.replace("Bearer ", "");
            final var claims = getAllClaimsFromToken(token);

            final var user = AuthUser.builder()
                    .id(Integer.parseInt(claims.get(CognitoService.CognitoFiled.USER_ID.getValue(), String.class)))
                    .uid(claims.get(CognitoService.CognitoFiled.UID.getValue(), String.class))
                    .email(claims.get(CognitoService.CognitoFiled.EMAIL.getValue(), String.class))
                    .mobile(claims.get(CognitoService.CognitoFiled.MOBILE.getValue(), String.class))
                    .name(claims.get(CognitoService.CognitoFiled.NAME.getValue(), String.class))
                    .role(findRole(claims.get(CognitoService.CognitoFiled.ROLE.getValue(), String.class)))
                    .image(claims.get(CognitoService.CognitoFiled.IMAGE.getValue(), String.class))
                    .build();

            return user;
        } catch (ExpiredJwtException | TokenExpiredException ex) {
            throw new ClanzException(HttpStatus.UNAUTHORIZED, "Authentication token is expired!");
        } catch (Exception e) {
            return null;
        }
    }

    private String findRole(String cognitoRole) {
        if (StringUtils.isBlank(cognitoRole)) {
            return null;
        }
        switch (cognitoRole) {
            case "1":
                return "USER";
            case "0":
                return "ADMIN";
            default:
                return null; //TODO: fix -> throw exception
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            token = token.replace("Bearer ", "");
            final var expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date(System.currentTimeMillis()));
        } catch (ExpiredJwtException | TokenExpiredException ex) {
            return true;
        } catch (Exception ex) {
            throw new ClanzException(HttpStatus.FORBIDDEN, "Token is invalid!");
        }
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            final var algorithm = Algorithm.RSA256(cognitoJwtService);
            final var jwtVerifier = JWT.require(algorithm)
                    .withIssuer(cognitoJwtService.getUrl())
                    .acceptLeeway(5) // 5 sec leeway window for nbf, iat and exp time checking
                    .build();

            // Verify the token
            jwtVerifier.verify(token);

            final var i = token.lastIndexOf('.');
            final var withoutSignature = token.substring(0, i + 1);
            return Jwts.parser()
                    .setAllowedClockSkewSeconds(6)
                    .parseClaimsJwt(withoutSignature)
                    .getBody();
        } catch (ExpiredJwtException | TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            throw new ClanzException(HttpStatus.FORBIDDEN, "Token is invalid!");
        }
    }
}