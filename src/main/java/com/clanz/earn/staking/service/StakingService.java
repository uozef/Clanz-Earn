package com.clanz.earn.staking.service;

import com.clanz.account.service.BrokerSubAccountService;
import com.clanz.base.util.HmacSHA256Signer;
import com.clanz.earn.staking.domain.*;
import com.clanz.earn.staking.domain.type.TxnType;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class StakingService {
    String baseUrl;
    WebClient webClient;
    final BrokerSubAccountService brokerSubAccountService;
    private static final String BINANCE_BASE_URL = "https://nd7f983ipe.execute-api.ap-southeast-2.amazonaws.com/";
    private static final String PRODUCT_PATH = "product=STAKING&recvWindow=60000&size=100&timestamp=";
    private static final String CURRENT = "&current=";
    private static final String SIGNATURE = "&signature=";
    private static final String APIKEY = "X-MBX-APIKEY";
    private static final String PRODUCT_ID = "&productId=";
    private static final String POSITION_ID = "&positionId=";
    private static final String AMOUNT = "&amount=";
    private static final String TXN_TYPE = "&txnType=";
    private static final String PURCHASE_QS = "product=STAKING&recvWindow=60000&size=50&timestamp=";

    public StakingService(BrokerSubAccountService brokerSubAccountService) {
        this.brokerSubAccountService = brokerSubAccountService;
    }

    @PostConstruct
    private void init() {
        this.baseUrl = System.getProperty("BINANACE_BASE_URL", BINANCE_BASE_URL);
        this.webClient = WebClient.builder().baseUrl(this.baseUrl).defaultHeader("Content-Type", new String[]{"application/json"}).build();
    }

    public ProductListDto getProducts(int userId, int current) {
            final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
            final var queryParams = PRODUCT_PATH + (System.currentTimeMillis()) + CURRENT + (current == 0 ? 1 : current);
            final var signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
            final var payload = "/sapi/v1/staking/productList?" + queryParams + SIGNATURE + signature;
            var res = webClient.get().uri(payload).header(APIKEY, subAccount.getApiKey())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(error ->
                                            Mono.error(new RuntimeException(error.toString()))
                                    )
                    )
                    .bodyToMono(String.class)
                    .block();
            res = "{\"products\":" + res + "}";
            return new Gson().fromJson(res, ProductListDto.class);

    }

    public PurchaseResponseDto purchase(int userId, PurchaseRequestDto purchaseRequestDto) {
        purchaseRequestDto.setTimestamp(System.currentTimeMillis());
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String queryParams = PURCHASE_QS + System.currentTimeMillis() + PRODUCT_ID + purchaseRequestDto.getProductId() + AMOUNT + purchaseRequestDto.getAmount();
        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/purchase?" + queryParams + SIGNATURE + signature;
        var res = webClient.post().uri(payload).header(APIKEY, subAccount.getApiKey())
                // .bodyValue(purchaseRequestDto)
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        return new Gson().fromJson(res, PurchaseResponseDto.class);
    }

    public PurchaseListResponseDto getPositions(int userId, String productId) {
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String queryParams = PRODUCT_PATH + System.currentTimeMillis();
        if (productId != null) {
            queryParams = queryParams +  PRODUCT_ID + productId;
        }

        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/position?" + queryParams + SIGNATURE + signature;
        var res = webClient.get().uri(payload).header(APIKEY, subAccount.getApiKey())
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        res = "{\"positions\":" + res + "}";
        return new Gson().fromJson(res, PurchaseListResponseDto.class);
    }

    public StakingRecordListDto getHistory(int userId, int current, TxnType txnType) {
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String queryParams = PRODUCT_PATH + System.currentTimeMillis() + CURRENT + current + TXN_TYPE + txnType;
        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/stakingRecord?" + queryParams + SIGNATURE + signature;

        var res = webClient.get().uri(payload).header(APIKEY, subAccount.getApiKey())
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        res = "{\"records\":" + res + "}";
        return new Gson().fromJson(res, StakingRecordListDto.class);
    }

    public RedeemResponseDto redeem(int userId, PurchaseRequestDto purchaseRequestDto) {
        purchaseRequestDto.setTimestamp(System.currentTimeMillis());
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String queryParams = PRODUCT_PATH + System.currentTimeMillis()+ PRODUCT_ID + purchaseRequestDto.getProductId()+ POSITION_ID + purchaseRequestDto.getPositionId();
        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/redeem?" + queryParams + SIGNATURE + signature;
        var res = webClient.post().uri(payload).header(APIKEY, subAccount.getApiKey())
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        return new Gson().fromJson(res, RedeemResponseDto.class);
    }

    public List<PersonalLeftQuotaResponseDto> getPersonalLeftQuotaList(int userId, String productId) {
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String apiEndpoint = "/sapi/v1/staking/personalLeftQuota?";
        long var10000 = System.currentTimeMillis();
        String queryParams = "product=STAKING&size=50&timestamp=" + var10000 + "&productId=" + productId;
        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/personalLeftQuota?" + queryParams + "&signature=" + signature;
        return (List) this.webClient.get().uri(payload, new Object[0]).accept(new MediaType[]{MediaType.APPLICATION_JSON}).retrieve().bodyToFlux(PersonalLeftQuotaResponseDto.class).collectList().block();
    }

    public RedeemResponseDto setAutoStaking(int userId, AutoStakingRequestDto autoStakingRequestDto) {
        autoStakingRequestDto.setTimestamp(System.currentTimeMillis());
        autoStakingRequestDto.setProduct("STAKING");
        final var subAccount = this.brokerSubAccountService.getSubAccountByUserId(userId);
        String apiEndpoint = "/sapi/v1/staking/setAutoStaking";
        String queryParams = "product=STAKING&size=50&timestamp=" + System.currentTimeMillis();
        String signature = HmacSHA256Signer.sign(queryParams, subAccount.getSecretKey());
        String payload = "/sapi/v1/staking/setAutoStaking" + queryParams + "&signature=" + signature;
        return (RedeemResponseDto) ((RequestBodySpec) this.webClient.post().uri(payload, new Object[0])).bodyValue(autoStakingRequestDto).accept(new MediaType[]{MediaType.APPLICATION_JSON}).retrieve().bodyToMono(RedeemResponseDto.class).block();
    }

    public static void main(String[] args) {
        try {
            WebClient webClient = WebClient.builder().baseUrl("https://nd7f983ipe.execute-api.ap-southeast-2.amazonaws.com").defaultHeader("Content-Type", new String[]{"application/json"}).build();
            String apikey = "90251aX8ftjh14Ya126tpqLYBMDkt9ot1o25AkUHwRuKPp1ScXaF2l6q2QTvqw4q";
            String secretkey = "0J6kKFH8cFQPAWpzg1AwgiOGC7aJMJNPsnYOlRMYAH8kfe4vfr7xBXjsiQBvcrIs";

            long var10000 = System.currentTimeMillis();
            String queryParams = "product=STAKING&size=50&timestamp=" + var10000 + "&current=" + 1 + "&recvWindow=60000";
            String signature = HmacSHA256Signer.sign(queryParams, secretkey);
            String payload = "/sapi/v1/staking/productList?" + queryParams + "&signature=" + signature;
            String res = webClient.get().uri(payload, new Object[0]).header("X-MBX-APIKEY", apikey)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve().bodyToMono(String.class).block();
            res = "{\"products\":" + res + "}";
            ProductListDto productDto = new Gson().fromJson(res, ProductListDto.class);
            ;
            // ProductDto productDto = new Gson().toJson(res);

            System.out.println(productDto);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
