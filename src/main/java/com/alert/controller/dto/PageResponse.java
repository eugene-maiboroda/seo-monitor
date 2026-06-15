package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PageResponse(
        Long pageId,
        String url,
        String currentH1,
        boolean h1Exists,
        LocalDateTime createdAt,
        LocalDateTime lastCheckedAt
) {

}