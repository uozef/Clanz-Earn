package com.clanz.earn.staking.service;

import com.clanz.earn.staking.domain.ProductDto;
import com.clanz.earn.staking.domain.ProductListFriendlyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StakingDataGenerator {
    private final StakingService stakingService;
    List<ProductListFriendlyDto> productList = new ArrayList<>();

    @Scheduled(fixedRate = 10000 * 60)
    public void generate() {
        try {
            List<ProductDto> products = new ArrayList<>();
            for (int i = 1; i < 15; i++) {
                final var list = this.stakingService.getProducts(1, i);
                if (list == null || list.getProducts() == null || list.getProducts().isEmpty()) break;
                products.addAll(list.getProducts());
            }

            Set<String> assets = new HashSet<>();
            for (var product : products) {
                assets.add(product.getDetail().getAsset());
            }
            List<ProductListFriendlyDto> productList = new ArrayList<>();
            for (var asset : assets) {
                final var list = products.stream().filter(s -> s.getDetail().getAsset().equalsIgnoreCase(asset)).collect(Collectors.toList());
                productList.add(ProductListFriendlyDto.builder().asset(asset).products(list).build());
            }
            this.productList = productList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<ProductListFriendlyDto> getProductList() {
        return productList;
    }
}
