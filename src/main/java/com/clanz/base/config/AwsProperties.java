package com.clanz.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String region;

    private Sm sm;

    private Kms kms;

    private Cw cw;

    private S3 s3;

    @Getter
    @Setter
    public static class Sm {

        private String dbArn;

        private String accessKeyId;

        private String masterPasswordArn;

        private String secretAccessKey;

        private String binanceArn;

        private String sendGridArn;

        private String twillioArn;

    }

    @Getter
    @Setter
    public static class Kms {

        private String accessKeyId;

        private String secretAccessKey;

    }

    @Getter
    @Setter
    public static class Cw {

        private String accessKeyId;

        private String secretAccessKey;

    }

    @Getter
    @Setter
    public static class S3 {

        private String bucketName;

        private String accessKey;

        private String secretKey;

    }
}
