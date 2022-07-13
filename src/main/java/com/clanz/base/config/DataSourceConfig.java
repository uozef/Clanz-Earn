package com.clanz.base.config;

import com.clanz.base.domain.dto.SMDBResponseDto;
import com.clanz.base.service.AWSSecretManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final AWSSecretManagerService awsSecurityManagerService;
    private SMDBResponseDto databaseCredential;

    @PostConstruct
    public void init() {
        databaseCredential = awsSecurityManagerService.getDatabaseCredential();
    }

    @Profile("!test")
    @Bean
    public DataSource getDataSource() {
        final var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");

        final var dbName = databaseCredential.getName();
        final var hostname = databaseCredential.getHost();
        final var port = databaseCredential.getPort();

        final var jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8";
        dataSourceBuilder.url(jdbcUrl);
        dataSourceBuilder.username(databaseCredential.getUsername());
        dataSourceBuilder.password(databaseCredential.getPassword());
        return dataSourceBuilder.build();
    }
}
