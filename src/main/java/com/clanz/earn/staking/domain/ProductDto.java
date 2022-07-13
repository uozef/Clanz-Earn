package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private String projectId;
    private ProductDetailDto detail;
    private ProductQuotaDto quota;
    private String sourceAsset;

}
