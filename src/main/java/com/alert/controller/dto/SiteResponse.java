package com.alert.controller.dto;

import lombok.Builder;

@Builder
public record SiteResponse(
    Long siteId,
    String name,
    String url
) {
}