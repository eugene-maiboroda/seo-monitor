package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RedirectPageResponse(
        String url,
        String redirectUrl,
        LocalDateTime checkedAt
) {
}