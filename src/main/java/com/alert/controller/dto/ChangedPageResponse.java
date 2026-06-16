package com.alert.controller.dto;

import com.alert.domain.dto.PageChangeType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChangedPageResponse(
        String url,
        PageChangeType changeType,
        String oldValue,
        String newValue,
        LocalDateTime checkedAt
) {
}