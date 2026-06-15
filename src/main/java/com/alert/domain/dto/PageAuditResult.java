package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record PageAuditResult(
        String url,
        String h1,

        boolean h1Exists,
        boolean redirected,

        String redirectUrl,
        String error
) {
}
