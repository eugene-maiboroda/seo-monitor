package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record FetchResult(
        int statusCode,
        String body,
        String redirectUrl,
        String errorMessage

) {

    public boolean isRedirect() {
        return statusCode == 301
                || statusCode == 302
                || statusCode == 307
                || statusCode == 308;

    }

    public boolean hasError() {
        return errorMessage != null;
    }

}