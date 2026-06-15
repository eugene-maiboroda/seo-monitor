package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorPageResponse(
        String url,
        String errorMessage,
        LocalDateTime checkedAt
) {
}