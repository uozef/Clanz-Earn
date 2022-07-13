package com.clanz.earn.staking.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListDto {

    List<ProductDto> products;

}
