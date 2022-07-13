package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StakingRecordListDto {

    private List<StakingRecordDto> records;
}
