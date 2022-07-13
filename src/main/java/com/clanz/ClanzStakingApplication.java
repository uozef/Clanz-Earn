package com.clanz;

import com.clanz.base.config.AwsProperties;
import com.clanz.base.config.CognitoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Properties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({AwsProperties.class, CognitoProperties.class, SendGridProperties.class})
public class ClanzStakingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ClanzStakingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        Properties bootProperties = new Properties();
        bootProperties.setProperty("server.servlet.context-path", "/v1");
        return application.sources(ClanzStakingApplication.class).properties(bootProperties);
    }

}