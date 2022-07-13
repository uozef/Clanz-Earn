package com.clanz.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cognito")
public class CognitoProperties {

    private String clientId;

    private String poolId;

    private String accessKey;

    private String secretKey;

}
