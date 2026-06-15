package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoH1PageResponse(
        String url,
        LocalDateTime checkedAt
) {
}
