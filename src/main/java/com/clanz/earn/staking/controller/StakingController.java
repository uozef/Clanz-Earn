package com.clanz.earn.staking.controller;

import com.clanz.base.domain.dto.ResponseDto;
import com.clanz.base.service.AuthUserService;
import com.clanz.earn.staking.domain.*;
import com.clanz.earn.staking.domain.type.TxnType;
import com.clanz.earn.staking.service.StakingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping({"/staking"})
@RestController
@RequiredArgsConstructor
public class StakingController {
    private static final Logger log = LogManager.getLogger(StakingController.class);
    private final StakingService stakingService;
    private final AuthUserService authService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(
            path = {"/products/{current}"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<ProductListDto>> getStakingProduct(@PathVariable("current") int current) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.getProducts(userId, current)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(
            path = {"/purchase"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<PurchaseResponseDto>> purchase(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.purchase(userId, purchaseRequestDto)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(
            path = {"/redeem"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<RedeemResponseDto>> redeem(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.redeem(userId, purchaseRequestDto)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(
            path = {"/position/{productId}"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<PurchaseListResponseDto>> getStakingPosition(@PathVariable("productId") String productId) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.getPositions(userId, productId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(
            path = {"/position"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<PurchaseListResponseDto>> getStakingAllPosition() {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.getPositions(userId, null)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(
            path = {"/history/{current}/{txnType}"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<StakingRecordListDto>> getHistory(@PathVariable("current") int current, @PathVariable("txnType") String txnType) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.getHistory(userId, current, TxnType.valueOf(txnType))));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(
            path = {"/quota/{productId}"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<List<PersonalLeftQuotaResponseDto>>> getPersonalLeftQuotaList(@PathVariable("productId") String productId) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.getPersonalLeftQuotaList(userId, productId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(
            path = {"/setAutoStaking"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<RedeemResponseDto>> setAutoStaking(@RequestBody AutoStakingRequestDto autoStakingRequestDto) {
        Integer userId = this.authService.getAuthUser().getId();
        return ResponseEntity.ok(ResponseDto.createSuccessMessage(this.stakingService.setAutoStaking(userId, autoStakingRequestDto)));
    }

    @GetMapping(
            path = {"/hi"},
            produces = {"application/json"}
    )
    public ResponseEntity<ResponseDto<RedeemResponseDto>> hi() {
        return ResponseEntity.ok(ResponseDto.createSuccessMessage("Hi,"));
    }

}
