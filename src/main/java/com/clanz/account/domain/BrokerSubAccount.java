package com.clanz.account.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BrokerSubAccounts")
public class BrokerSubAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "AccountId")
    private String accountId;

    @Column(name = "ApiKey")
    private String apiKey;

    @Column(name = "SecretKey")
    private String secretKey;

    @Column(name = "EncApiKey")
    private byte[] encApiKey;

    @Column(name = "EncSecretKey")
    private byte[] encSecretKey;

}
