package com.alert.client;

import com.alert.domain.dto.FetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class HttpFetchClient {

    private final RestClient restClient;

    public FetchResult fetchBody(String url) {
        ResponseEntity<String> response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class);

        return FetchResult.builder()
                .statusCode(response.getStatusCode().value())
                .body(response.getBody())
                .redirectUrl(response.getHeaders().getLocation() != null ? response.getHeaders().getLocation().toString() : null)
                .errorMessage(response.getStatusCode().isError() ? "HTTP error: " + response.getStatusCode() : null)
                .build();
    }

    public boolean exists(String url) {
        try {
            restClient.get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}