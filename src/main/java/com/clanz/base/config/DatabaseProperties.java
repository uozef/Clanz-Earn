package com.clanz.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "database")
public class DatabaseProperties {
    private String name;
    private String host;
    private String port;
    private String username;
    private String password;
}
