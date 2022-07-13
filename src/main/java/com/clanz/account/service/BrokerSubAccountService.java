package com.clanz.account.service;

import com.clanz.account.domain.BrokerSubAccount;
import com.clanz.account.repository.BrokerSubAccountRepository;
import com.clanz.base.exception.ClanzException;
import com.clanz.base.service.AWSKMSManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class BrokerSubAccountService {
    private final BrokerSubAccountRepository subAccountRepository;
    private final AWSKMSManagerService awskmsManagerService;

    private BrokerSubAccount getSubAccount(int userId) {
        try {
            final var subAccount = subAccountRepository.findFirstByUserId(userId);
            if (subAccount == null) {
                return null;
            }

            if (StringUtils.isNotEmpty(subAccount.getApiKey()) && subAccount.getApiKey().length() > 10) {
                return subAccount;
            }

            final var secretKey = awskmsManagerService.decryptData(new ByteArrayInputStream(subAccount.getEncSecretKey()));
            final var apiKey = awskmsManagerService.decryptData(new ByteArrayInputStream(subAccount.getEncApiKey()));

            subAccount.setApiKey(apiKey);
            subAccount.setSecretKey(secretKey);
            return subAccount;
        } catch (Exception e) {
            throw new ClanzException(e, HttpStatus.BAD_REQUEST, "Operation failed!", "Generate secretKey and ApiKey is failed in method getSubAccount");
        }
    }

    public BrokerSubAccount getSubAccountByUserId(int userId) {
        String apikey = "90251aX8ftjh14Ya126tpqLYBMDkt9ot1o25AkUHwRuKPp1ScXaF2l6q2QTvqw4q";
        String secretkey = "0J6kKFH8cFQPAWpzg1AwgiOGC7aJMJNPsnYOlRMYAH8kfe4vfr7xBXjsiQBvcrIs";
        final var subAccount = BrokerSubAccount.builder().apiKey(apikey).secretKey(secretkey).build();
       /* final var subAccount = getSubAccount(userId);
        if (subAccount == null) {
            throw new ClanzException(HttpStatus.BAD_REQUEST, "The sub account is empty", "The sub account is empty method getSubAccountByUserId");
        }*/
        return subAccount;
    }

}
