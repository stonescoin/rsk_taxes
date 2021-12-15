package com.ruho.rsk.http;

import com.ruho.rsk.domain.RskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TransactionsFetcherService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String basicAuth;
    private final String walletAddress;


    public TransactionsFetcherService(RestTemplateBuilder restTemplateBuilder,
            @Value("${covalenthq.basicAuth}") String basicAuth,
            @Value("${rsk.wallet.address}") String walletAddress,
            @Value("${covalenthq.base.url}") String baseUrl) {

        this.baseUrl = baseUrl;
        this.basicAuth = basicAuth;
        this.walletAddress = walletAddress;

        restTemplate = restTemplateBuilder.build();
    }

    public RskDto fetchTransactions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + this.basicAuth);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("https")
                .host(this.baseUrl)
                .path("/v1/30/address/" + this.walletAddress + "/transactions_v2")
                .build();

        ResponseEntity<RskDto> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, RskDto.class);

        return response.getBody();
    }
}
