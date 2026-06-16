package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record SiteResponseDto(
        Long id,
        String url,
        String name,
        int intervalHours
) {
}