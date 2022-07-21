package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageDto {
    private String code;
    private String msg;
}
