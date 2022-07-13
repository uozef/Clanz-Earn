package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuotaDto {

    private String totalPersonalQuota;
    private String minimum;

}
