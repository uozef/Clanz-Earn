package com.clanz.base.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMDBResponseDto {
    private String username;
    private String password;
    private String host;
    private String port;
    private String name;
}