package com.clanz.base.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Principal {
    private Integer id;
    private String uid;
    private String email;
    private String mobile;
    private String name;
    private String role;
    private String image;
    private String accessToken;
    private boolean kyc;


    public Set<SimpleGrantedAuthority> generatePermissions() {
        final Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_" + role);

        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
