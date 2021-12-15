package com.ruho.rsk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruho.rsk.domain.RskDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class TransactionsFetcherService {

    private final RestTemplate restTemplate;
    private final String baseUrl;


    public TransactionsFetcherService(RestTemplateBuilder restTemplateBuilder,
                                      @Value("${covalenthq.basic.url}") String baseUrl
    ) {

        this.baseUrl = baseUrl;

        restTemplate = restTemplateBuilder.build();
    }

    public RskDto fetchTransactions(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (Reader reader = new FileReader(filePath)) {
            return mapper.readValue(reader, RskDto.class);
        }
    }

    public RskDto fetchTransactions(String walletAddress, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        String auth = apiKey + ":";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII) );
        headers.add("Authorization", "Basic " + new String(encodedAuth));

        HttpEntity<String> request = new HttpEntity<>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("https")
                .host(this.baseUrl)
                .path("/v1/30/address/" + walletAddress + "/transactions_v2/")
                .build();

        ResponseEntity<RskDto> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, RskDto.class);
        System.out.println("transactions received!");
        return response.getBody();
    }
}
