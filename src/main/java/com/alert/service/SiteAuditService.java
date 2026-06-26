package com.alert.service;

import com.alert.domain.dto.PageComparisonResult;
import com.alert.domain.dto.SiteAuditResult;
import com.alert.domain.entity.CheckStatus;
import com.alert.domain.entity.SiteCheckEntity;
import com.alert.domain.entity.SiteEntity;
import com.alert.parser.SiteHealthChecker;
import com.alert.repository.PageRepository;
import com.alert.repository.SiteCheckerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteAuditService {

    private final SiteHealthChecker healthChecker;
    private final PageService pageService;
    private final SiteCheckerRepository siteCheckerRepository;
    private final NotificationService notificationService;
    private final PageRepository pageRepository;

    public void audit(SiteEntity site) {
        List<PageComparisonResult> pageAuditResults = Collections.emptyList();
        boolean pageAuditPassed = false;
        long pagesCount = 0;
        SiteAuditResult siteAudit = healthChecker.audit(site.getUrl());

        if (siteAudit.siteAvailable() && siteAudit.sitemapExists()) {
            try {
                pageAuditResults = pageService.audit(site);
                pageAuditPassed = true;
                pagesCount = pageRepository.countBySiteId(site.getId());
                log.info("Site {} audited. {} pages found.", site.getUrl(), pagesCount);
            } catch (Exception e) {
                log.warn("Page audit failed for site {}: {}", site.getUrl(), e.getMessage());
            }
        }
        boolean changed = pageAuditResults.stream().anyMatch(PageComparisonResult::changed);
        SiteCheckEntity siteCheckEntity = SiteCheckEntity.builder()
                .siteId(site.getId())
                .siteAvailable(siteAudit.siteAvailable())
                .sitemapExists(siteAudit.sitemapExists())
                .robotsExists(siteAudit.robotsExists())
                .pageAuditPassed(pageAuditPassed)
                .status(checkStatus(siteAudit, pageAuditPassed, changed))
                .errorMessage(siteAudit.error())
                .pageCount(pagesCount)
                .build();

        siteCheckerRepository.save(siteCheckEntity);
        notificationService.send(siteCheckEntity, pageAuditResults, site.getName(), site.getUrl());
    }

    private CheckStatus checkStatus(SiteAuditResult result, boolean pageAuditPassed, boolean changed) {
        if (!result.siteAvailable()) {
            return CheckStatus.ERROR;
        }

        if (!result.sitemapExists()
                || !result.robotsExists()
                || !pageAuditPassed
                || changed) {
            return CheckStatus.WARNING;
        }
        return CheckStatus.UP;

    }

}
