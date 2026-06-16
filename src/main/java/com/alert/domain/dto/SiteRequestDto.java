package com.alert.domain.dto;

public record SiteRequestDto(
        String url,
        String name,
        int intervalHours
) {
}
