package com.clanz.account.repository;

import com.clanz.account.domain.BrokerSubAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerSubAccountRepository extends CrudRepository<BrokerSubAccount, Integer> {
    BrokerSubAccount findFirstByApiKeyAndSecretKey(String apiKey, String secretKey);

    BrokerSubAccount findFirstByUserId(Integer secretKey);
}
