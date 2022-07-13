package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionResponseDto {

    private String positionId;
    private String projectId;
    private String asset;
    private String amount;
    private String purchaseTime;
    private String duration;
    private String accrualDays;
    private String rewardAsset;
    private String APY;
    private String rewardAmt;
    private String extraRewardAsset;
    private String extraRewardAPY;
    private String estExtraRewardAmt;
    private String nextInterestPay;
    private String nextInterestPayDate;
    private String payInterestPeriod;
    private String redeemAmountEarly;
    private String interestEndDate;
    private String deliverDate;
    private String redeemPeriod;
    private String redeemingAmt;
    private String partialAmtDeliverDate;
    private String canRedeemEarly;
    private String renewable;
    private String type;
    private String status;

}
