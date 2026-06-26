package com.alert.service;

import com.alert.controller.dto.*;
import com.alert.domain.entity.PageCheckEntity;
import com.alert.domain.entity.PageEntity;
import com.alert.domain.entity.SiteEntity;
import com.alert.domain.exeption.SiteNotFoundException;
import com.alert.repository.PageCheckRepository;
import com.alert.repository.PageRepository;
import com.alert.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PageRepository pageRepository;
    private final PageCheckRepository pageCheckRepository;
    private final SiteRepository siteRepository;
    private final Clock clock;

    public SummaryResponse getSummary(Long siteId) {

        SiteEntity site = siteRepository.findById(siteId)
                .orElseThrow(() -> new SiteNotFoundException("Site not found"));

        LocalDateTime lastCheckAt = site.getLastCheckAt() != null ? site.getLastCheckAt() : LocalDateTime.MIN;

        return SummaryResponse.builder()
                .pages(pageRepository.countBySiteId(siteId))
                .noH1Page(pageRepository.countBySiteIdAndH1ExistsFalse(siteId))
                .redirectPage(pageRepository.countBySiteIdAndRedirectedTrue(siteId))
                .errorPage(pageRepository.countBySiteIdAndErrorMessageIsNotNull(siteId))
                .changesPage(pageCheckRepository.countBySiteId(siteId))
                .lastAudit(lastCheckAt)
                .build();
    }

    public Page<PageResponse> getPages(Long siteId, int page, int size) {
        return pageRepository.findBySiteId(siteId, PageRequest.of(page, size))
                .map(this::toPageResponse);
    }

    public List<NoH1PageResponse> getNoH1(Long siteId) {
        return pageRepository.findBySiteIdAndH1ExistsFalse(siteId).stream()
                .map(this::toNoH1PageResponse)
                .toList();
    }

    public List<RedirectPageResponse> getRedirects(Long siteId) {
        return pageRepository.findBySiteIdAndRedirectedTrue(siteId).stream()
                .map(this::toRedirectPageResponse)
                .toList();
    }

    public List<ErrorPageResponse> getErrors(Long siteId) {
        return pageRepository.findBySiteIdAndErrorMessageIsNotNull(siteId).stream()
                .map(this::toErrorPageResponse)
                .toList();
    }

    public Page<ChangedPageResponse> getChanges(Long siteId, Integer days, int page, int size) {
        if (days == null) {
            return pageCheckRepository.findBySiteId(siteId, PageRequest.of(page, size))
                    .map(this::toChangedPageResponse);
        }

        LocalDateTime from = LocalDateTime.now(clock).minusDays(days);
        return pageCheckRepository.findBySiteIdAndCheckedAtAfter(siteId, from, PageRequest.of(page, size))
                .map(this::toChangedPageResponse);
    }

    private NoH1PageResponse toNoH1PageResponse(PageEntity pageEntity) {
        return NoH1PageResponse.builder()
                .url(pageEntity.getUrl())
                .build();
    }

    private RedirectPageResponse toRedirectPageResponse(PageEntity entity) {
        return RedirectPageResponse.builder()
                .url(entity.getUrl())
                .redirectUrl(entity.getRedirectUrl())
                .checkedAt(entity.getLastCheckedAt())
                .build();
    }

    private ErrorPageResponse toErrorPageResponse(PageEntity entity) {
        return ErrorPageResponse.builder()
                .url(entity.getUrl())
                .errorMessage(entity.getErrorMessage())
                .checkedAt(entity.getLastCheckedAt())
                .build();
    }

    private ChangedPageResponse toChangedPageResponse(PageCheckEntity entity) {
        return ChangedPageResponse.builder()
                .url(entity.getUrl())
                .changeType(entity.getChangeType())
                .oldValue(entity.getOldValue())
                .newValue(entity.getNewValue())
                .checkedAt(entity.getCheckedAt())
                .build();
    }

    private PageResponse toPageResponse(PageEntity page) {
        return PageResponse.builder()
                .pageId(page.getId())
                .url(page.getUrl())
                .currentH1(page.getCurrentH1())
                .h1Exists(page.isH1Exists())
                .createdAt(page.getCreatedAt())
                .lastCheckedAt(page.getLastCheckedAt())
                .build();
    }
}