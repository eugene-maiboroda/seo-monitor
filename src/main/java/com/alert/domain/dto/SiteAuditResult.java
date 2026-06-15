package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record SiteAuditResult(
        boolean siteAvailable,
        boolean sitemapExists,
        boolean robotsExists,
        String error
) {}