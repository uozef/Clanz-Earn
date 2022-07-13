package com.clanz.base.filter;

import com.clanz.base.domain.dto.ResponseDto;
import com.clanz.base.exception.ClanzException;
import com.clanz.base.service.AuthenticationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenService authenticationTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            final var authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isEmpty(authorizationToken)) {
                chain.doFilter(request, response);
                return;
            }

            final var tokenExpired = authenticationTokenService.isTokenExpired(authorizationToken);
            if (tokenExpired) {
                throw new ClanzException(HttpStatus.UNAUTHORIZED, "Authentication token is expired!");
            }

            final var authUser = authenticationTokenService.getAuthUserFromAuthorizationToken(authorizationToken);
            if (authUser == null) {
                throw new ClanzException(HttpStatus.FORBIDDEN, "Token is invalid!");
            }

            final var accessToken = request.getHeader("Access-Token");
            if (StringUtils.isEmpty(accessToken)) {
                throw new ClanzException(HttpStatus.FORBIDDEN, "Token is invalid!");
            }
            authUser.setAccessToken(accessToken);

            final var authenticationToken = new UsernamePasswordAuthenticationToken(
                    authUser, null, authUser.generatePermissions());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);
        } catch (ClanzException ex) {
            writeErrorResponse(response, ex);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, ClanzException ex) {
        try {
            final var dto = ResponseDto.builder()
                    .status("error")
                    .message(ex.getMessage())
                    .build();
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.setStatus(ex.getStatusCode().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        } catch (Exception e) {
            log.catching(e);
        }
    }
}
