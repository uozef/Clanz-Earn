package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseResponseDto {
    private String positionId;
    private boolean success;

}
