package com.clanz.base.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class AWSCredentialsProviderUtil {

    public static AwsCredentialsProvider getAwsCredentialsProvider(String key, String secretKey) {
        if (PropertyUtils.getProperty("isLocal") != null) {
            return StaticCredentialsProvider.create(AwsBasicCredentials.create(key, secretKey));
        }
        return InstanceProfileCredentialsProvider.create();
    }

}
