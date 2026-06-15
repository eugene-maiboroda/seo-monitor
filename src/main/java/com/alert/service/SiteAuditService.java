package com.alert.service;

import com.alert.domain.dto.SiteAuditResult;
import com.alert.domain.entity.CheckStatus;
import com.alert.domain.entity.SiteCheckEntity;
import com.alert.domain.entity.SiteEntity;
import com.alert.parser.SiteHealthChecker;
import com.alert.repository.SiteCheckerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteAuditService {

    private final SiteHealthChecker healthChecker;
    private final PageAuditService pageAuditService;
    private final SiteCheckerRepository siteCheckerRepository;
    private final NotificationService notificationService;

    public void audit(SiteEntity site) {

        SiteAuditResult siteAudit = healthChecker.audit(site.getUrl());

        boolean pageAuditPassed = false;

        if (siteAudit.siteAvailable() && siteAudit.sitemapExists()) {
            pageAuditPassed = pageAuditService.audit(site);
        }
        SiteCheckEntity siteCheckEntity = SiteCheckEntity.builder()
                .siteId(site.getId())
                .siteAvailable(siteAudit.siteAvailable())
                .sitemapExists(siteAudit.sitemapExists())
                .robotsExists(siteAudit.robotsExists())
                .pageAuditPassed(pageAuditPassed)
                .status(checkStatus(siteAudit, pageAuditPassed))
                .errorMessage(siteAudit.error())
                .build();

        siteCheckerRepository.save(siteCheckEntity);

        notificationService.send(siteCheckEntity, site.getName(), site.getUrl());

    }

    private CheckStatus checkStatus(SiteAuditResult result, boolean pageAuditPassed) {
        if (!result.siteAvailable()) {
            return CheckStatus.ERROR;
        }

        if (!result.sitemapExists()
                || !result.robotsExists()
                || !pageAuditPassed) {
            return CheckStatus.WARNING;
        }
        return CheckStatus.UP;

    }

}
