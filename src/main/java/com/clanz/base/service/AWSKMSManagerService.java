package com.clanz.base.service;


import com.clanz.base.domain.KMSObject;
import com.clanz.base.config.AwsProperties;
import com.clanz.base.domain.type.KeyType;
import com.clanz.base.util.AWSCredentialsProviderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class AWSKMSManagerService {

    static String key = "";
    static String secretKey = "";
    KmsClient kmsClient;
    @Autowired
    private AwsProperties awsProperties;

    @PostConstruct
    private void init() {
        AWSKMSManagerService.key = awsProperties.getKms().getAccessKeyId();
        AWSKMSManagerService.secretKey = awsProperties.getKms().getSecretAccessKey();

        kmsClient = KmsClient.builder()
                .credentialsProvider(AWSCredentialsProviderUtil.getAwsCredentialsProvider(key, secretKey))
                .region(Region.AP_SOUTHEAST_2).build();
    }

    private SdkBytes encrypt(SdkBytes jsonString, KeyType keyType) {
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(keyType.getKeyId()).plaintext(jsonString).build();
        EncryptResponse encryptResponse = kmsClient
                .encrypt(encryptRequest);
        return encryptResponse.ciphertextBlob();
    }

    private SdkBytes decrypt(SdkBytes encryptedJsonString) {
        DecryptRequest decryptRequest = DecryptRequest.builder()
                .ciphertextBlob(encryptedJsonString).build();
        DecryptResponse decryptResponse = this.kmsClient
                .decrypt(decryptRequest);
        return decryptResponse.plaintext();
    }

    private KMSObject encryptUsingDataKey(String input, KeyType keyType) throws Exception {
        SdkBytes jsonString = toSdkBytes(input);
        GenerateDataKeyRequest generateDataKeyRequest = GenerateDataKeyRequest
                .builder().keyId(keyType.getKeyId())
                .keySpec(DataKeySpec.AES_128).build();
        GenerateDataKeyResponse generateDataKeyResponse = this.kmsClient
                .generateDataKey(generateDataKeyRequest);

        SecretKeySpec key = new
                SecretKeySpec(generateDataKeyResponse.plaintext().asByteArray(),
                "AES");
        Cipher cipher;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encodedSecret = cipher.doFinal(jsonString.asByteArray());
        byte[] encryptedDataKey = key.getEncoded();
        return new KMSObject(encodedSecret, encryptedDataKey);
        //return SdkBytes.fromByteArray(cipher.doFinal(jsonString.asByteArray())).asUtf8String();
        // return StringUtils.newStringUtf8(cipher.doFinal(jsonString.asByteArray()));

        // byte[] encodedSecret = cipher.doFinal(jsonString.asByteArray());
        //  byte[] encryptedDataKey = key.getEncoded();
    }

    private String decryptUsingDataKey(KMSObject kmsObject) throws Exception {
        SdkBytes sdkKeyBytes = SdkBytes.fromByteArray(kmsObject.getKey());
        SdkBytes sdkContentBytes = SdkBytes.fromByteArray(kmsObject.getContent());

        DecryptRequest decryptRequest = DecryptRequest.builder().ciphertextBlob(sdkKeyBytes).build();
        DecryptResponse decryptResponse = this.kmsClient.decrypt(decryptRequest);

        SecretKeySpec secretKeySpec = new SecretKeySpec(decryptResponse.plaintext().asByteArray(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        String res = SdkBytes.fromByteArray(cipher.doFinal(sdkContentBytes.asByteArray())).asUtf8String();
        System.out.println(res);
        return res;
    }

    private SdkBytes toSdkBytes(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes
                (StandardCharsets.UTF_8));
        return SdkBytes.fromInputStream(in);
    }

    public InputStream encryptData(String data) {
        SdkBytes sdkBytes = SdkBytes.fromUtf8String(data);
        SdkBytes enc = encrypt(sdkBytes, KeyType.USER_2FA_PROD_KEY);
        return enc.asInputStream();
    }

    public String decryptData(InputStream data) {
        SdkBytes enc = decrypt(SdkBytes.fromInputStream(data));
        return enc.asUtf8String();
    }

}
