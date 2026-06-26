package com.alert.scheduler;

import com.alert.domain.entity.SiteEntity;
import com.alert.repository.SiteRepository;
import com.alert.service.SiteAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.scheduler.enabled", havingValue = "true")
@Slf4j
public class SiteCheckScheduler {

    private final SiteRepository siteRepository;
    private final SiteAuditService auditService;
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Kyiv");

    @Scheduled(fixedRateString = "${app.scheduler.fixed-rate-ms}")
    public void schedule() {
        log.info("Scheduled task started");
        List<SiteEntity> sites = siteRepository.findAll();

        for (SiteEntity site : sites) {
            if (!site.isEnabled()) {
                continue;
            }

            if (!shouldAudit(site)) {
                continue;
            }

            auditService.audit(site);
            site.setLastCheckAt(LocalDateTime.now(ZONE_ID));
            siteRepository.save(site);
        }

        log.info("Scheduled task finished");

    }

    private boolean shouldAudit(SiteEntity site) {
        if (site.getLastCheckAt() == null) {
            return true;
        }

        return site.getLastCheckAt()
                .plusHours(site.getCheckIntervalHours())
                .isBefore(LocalDateTime.now());
    }
}
