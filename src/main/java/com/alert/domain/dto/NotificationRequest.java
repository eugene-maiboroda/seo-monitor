package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record NotificationRequest(

        Long siteId,
        String siteName,
        String url,
        String status,

        boolean siteAvailable,
        boolean sitemapExists,
        boolean robotsExists,
        boolean pageAuditPassed,

        String errorMessage
) {}