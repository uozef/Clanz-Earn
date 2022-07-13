package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseRequestDto {

    private String product;
    private String positionId;
    private String productId;
    private BigDecimal amount;
    private long timestamp;

}
