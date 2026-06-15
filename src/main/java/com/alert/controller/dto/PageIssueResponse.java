package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PageIssueResponse(
        String url,
        LocalDateTime checkedAt,
        String details
) {
}