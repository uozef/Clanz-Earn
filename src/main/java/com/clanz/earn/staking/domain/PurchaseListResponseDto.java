package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PurchaseListResponseDto {
    private List<PositionResponseDto> positions;

}
