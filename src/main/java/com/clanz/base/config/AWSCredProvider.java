package com.clanz.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;

@Configuration
public class AWSCredProvider {

    @Bean
    @Primary
   public AwsCredentialsProvider credProvider() {
        return InstanceProfileCredentialsProvider.create();
    }

}
