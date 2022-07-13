package com.clanz.earn.staking.domain;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoStakingRequestDto {
    private String product;
    private String positionId;
    private boolean renewable;
    private long timestamp;

}
