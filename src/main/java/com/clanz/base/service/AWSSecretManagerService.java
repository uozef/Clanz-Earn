package com.clanz.base.service;

import com.clanz.base.config.AWSCredProvider;
import com.clanz.base.config.AwsProperties;
import com.clanz.base.domain.dto.SMDBResponseDto;
import com.clanz.base.util.AWSCredentialsProviderUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.Base64;

@Service
public class AWSSecretManagerService {

    @Autowired
    AWSCredProvider credProvider;

    @Autowired
    AwsProperties properties;

    String key = "";
    String secretKey = "";

    public SMDBResponseDto getDatabaseCredential() {
        Region region = Region.of(properties.getRegion());
        String secretName = properties.getSm().getDbArn();
        SMDBResponseDto responseDTO = null;

        this.key = properties.getSm().getAccessKeyId();
        this.secretKey = properties.getSm().getSecretAccessKey();

        SecretsManagerClient client = SecretsManagerClient.builder()
                .credentialsProvider(AWSCredentialsProviderUtil.getAwsCredentialsProvider(key, secretKey))
                // .credentialsProvider(credProvider.credProvider())
                .region(region)
                .build();

        String secret, decodedBinarySecret;
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse = null;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException | InternalServiceErrorException | InvalidParameterException |
                 InvalidRequestException | ResourceNotFoundException e) {
            throw e;
        }

        // Decrypts secret using the associated KMS key.
        // Depending on whether the secret is a string or binary, one of these fields will be populated.
        if (getSecretValueResponse.secretString() != null) {
            secret = getSecretValueResponse.secretString();
            responseDTO = new Gson().fromJson(secret, SMDBResponseDto.class);

        } else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
        }

        return responseDTO;
        // Your code goes here.
    }
}