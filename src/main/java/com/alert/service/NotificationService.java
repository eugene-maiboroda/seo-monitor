package com.alert.service;

import com.alert.domain.dto.NotificationRequest;
import com.alert.domain.dto.PageComparisonResult;
import com.alert.domain.entity.CheckStatus;
import com.alert.domain.entity.SiteCheckEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @Value("${app.notification.webhook-url}")
    private String webhookUrl;

    @Value("${app.notification.dashboard-url}")
    private String dashboardUrl;

    private final RestClient restClient;

    public void send(SiteCheckEntity siteCheck, List<PageComparisonResult> pageChanged, String name, String url) {

        boolean pagesChanged = pageChanged.stream()
                .anyMatch(PageComparisonResult::changed);

        NotificationRequest request = NotificationRequest.builder()
                .siteName(name)
                .url(url)
                .status(siteCheck.getStatus().name())
                .siteAvailable(siteCheck.isSiteAvailable())
                .sitemapExists(siteCheck.isSitemapExists())
                .robotsExists(siteCheck.isRobotsExists())
                .pageAuditPassed(siteCheck.isPageAuditPassed())
                .pagesChanged(pagesChanged)
                .errorMessage(siteCheck.getErrorMessage())
                .dashboardUrl(dashboardUrl)
                .build();

        restClient.post()
                .uri(webhookUrl)
                .body(request)
                .retrieve()
                .toBodilessEntity();
        log.info(webhookUrl);
        log.info("Notification sent to webhook");
    }
}
