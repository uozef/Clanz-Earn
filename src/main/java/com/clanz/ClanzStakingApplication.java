package com.clanz;

import com.clanz.base.config.AwsProperties;
import com.clanz.base.config.CognitoProperties;
import com.clanz.base.config.DatabaseProperties;
import com.clanz.earn.staking.service.StakingDataGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({AwsProperties.class, CognitoProperties.class, SendGridProperties.class, DatabaseProperties.class})
@EnableScheduling
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
