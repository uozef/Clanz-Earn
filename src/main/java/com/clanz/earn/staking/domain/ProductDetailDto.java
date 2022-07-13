package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDto {
    private String asset;
    private String rewardAsset;
    private String apy;
    private int duration;
    private boolean renewable;

}
