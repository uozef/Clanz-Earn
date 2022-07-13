package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StakingRecordDto {

    private String positionId;
    private long time;
    private String asset;
    private String amount;
    private String project;
    private String lockPeriod;
    private String deliverDate;
    private String type;
    private String status;
}
