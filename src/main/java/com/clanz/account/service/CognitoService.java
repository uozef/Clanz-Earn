package com.clanz.account.service;

import com.clanz.base.config.AwsProperties;
import com.clanz.base.config.CognitoProperties;
import com.clanz.base.util.AWSCredentialsProviderUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Log4j2
public class CognitoService {
    private String cognitoClientId;
    private String cognitoPoolId;
    private final AwsProperties awsProperties;
    private final CognitoProperties cognitoProperties;
    private CognitoIdentityProviderClient cognitoProvider;

    @PostConstruct
    private void init() {
        cognitoClientId = cognitoProperties.getClientId();
        cognitoPoolId = cognitoProperties.getPoolId();

        final var cognitoAccessKey = cognitoProperties.getAccessKey();
        final var cognitoSecretKey = cognitoProperties.getSecretKey();
        cognitoProvider = CognitoIdentityProviderClient.builder()
                .region(Region.of(awsProperties.getRegion()))
               // .credentialsProvider(AWSCredentialsProviderUtil.getAwsCredentialsProvider(cognitoAccessKey, cognitoSecretKey))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(cognitoAccessKey, cognitoSecretKey)))
                .build();
    }

    Integer parseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public enum CognitoFiled {
        USER_ID("custom:user_id"),
        UID("custom:uid"),
        EMAIL("email"),
        MOBILE("phone_number"),
        NAME("name"),
        BASE_CURRENCY_CODE("custom:base_currency"),
        ROLE("custom:role"),
        COUNTRY_CODE("custom:country_code"),
        ADDRESS("custom:address"),
        STATE("custom:state"),
        POSTAL_CODE("custom:postal_code"),
        BIRTH_DATE("birthdate"),
        IMAGE("picture"),
        ANSWER_SECURITY_QUESTION_1("custom:answer_question_1"),
        ANSWER_SECURITY_QUESTION_2("custom:answer_question_2"),
        MFA_CODE("custom:mfa_code"),
        MFA_ENABLED("custom:mfa_enabled"),
        ;

        @Getter
        private final String value;

        CognitoFiled(String value) {
            this.value = value;
        }

        public static CognitoFiled find(String value) {
            for (var filed : values()) {
                if (filed.value.equals(value)) {
                    return filed;
                }
            }
            return null;
        }
    }

}