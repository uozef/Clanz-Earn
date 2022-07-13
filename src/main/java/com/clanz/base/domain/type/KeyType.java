package com.clanz.base.domain.type;


import com.clanz.base.util.PropertyUtils;

public enum KeyType {
    USER_2FA_PROD_KEY(PropertyUtils.getProperty("USER_2FA_PROD_KEY")),
    BN_SUBACCOUNT_KEY("c349d674-a2bc-4d6a-963e-e9c27de05f78"),
    BN_MASTER_ACCOUNT_KEY("6c8ac71d-312d-4e3e-875d-3aa7ec7b368b");

    private final String keyId;

    KeyType(String id) {
        keyId = id;
    }

    public String getKeyId() {
        return keyId;
    }
}
