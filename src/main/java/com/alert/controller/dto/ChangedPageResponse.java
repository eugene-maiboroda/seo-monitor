package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChangedPageResponse(
        String url,
        String previousH1,
        String currentH1,
        LocalDateTime checkedAt
) {
}