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
@Profile("!test")
public class DataSourceConfig {
    private final AWSSecretManagerService awsSecurityManagerService;
    private final DatabaseProperties databaseProperties;


    @Profile("!dev")
    @Bean
    public DataSource getDataSource() {
        final var databaseCredential = awsSecurityManagerService.getDatabaseCredential();
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

    @Profile("dev")
    @Bean
    public DataSource getDevDataSource() {
        final var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");

        final var dbName = databaseProperties.getName();
        final var hostname = databaseProperties.getHost();
        final var port = databaseProperties.getPort();

        final var jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false&useUnicode=yes&characterEncoding=UTF-8";
        dataSourceBuilder.url(jdbcUrl);
        dataSourceBuilder.username(databaseProperties.getUsername());
        dataSourceBuilder.password(databaseProperties.getPassword());
        return dataSourceBuilder.build();
    }
}
