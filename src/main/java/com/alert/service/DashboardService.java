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

        LocalDateTime yesterday = LocalDateTime.now(clock).minusHours(24);
        LocalDateTime lastCheckAt = site.getLastCheckAt() != null ? site.getLastCheckAt() : LocalDateTime.MIN;

        return SummaryResponse.builder()
                .pages(pageRepository.countBySiteId(siteId))
                .noH1Page(pageRepository.countBySiteIdAndH1ExistsFalse(siteId))
                .redirectPage(pageCheckRepository.countByRedirectedTrue(siteId))
                .errorPage(pageCheckRepository.countBySiteIdAndErrorMessageIsNotNullAndCheckedAtAfter(siteId, yesterday))
                .changesPage(pageCheckRepository.countBySiteIdAndH1ChangedTrueAndCheckedAtAfter(siteId, yesterday))
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
        return pageCheckRepository.findBySiteIdAndRedirectedTrue(siteId).stream()
                .map(this::toRedirectPageResponse)
                .toList();
    }

    public List<ErrorPageResponse> getErrors(Long siteId) {
        LocalDateTime yesterday = LocalDateTime.now(clock).minusHours(24);
        return pageCheckRepository.findBySiteIdAndErrorMessageIsNotNullAndCheckedAtAfter(siteId, yesterday).stream()
                .map(this::toErrorPageResponse)
                .toList();
    }

    public List<ChangedPageResponse> getChanges(Long siteId) {
        LocalDateTime yesterday = LocalDateTime.now(clock).minusHours(24);
        return pageCheckRepository.findBySiteIdAndH1ChangedTrueAndCheckedAtAfter(siteId, yesterday).stream()
                .map(this::toChangedPageResponse)
                .toList();
    }

    private NoH1PageResponse toNoH1PageResponse(PageEntity pageEntity) {
        return NoH1PageResponse.builder()
                .url(pageEntity.getUrl())
                .build();
    }

    private RedirectPageResponse toRedirectPageResponse(PageCheckEntity entity) {
        return RedirectPageResponse.builder()
                .url(entity.getUrl())
                .redirectUrl(entity.getRedirectUrl())
                .checkedAt(entity.getCheckedAt())
                .build();
    }

    private ErrorPageResponse toErrorPageResponse(PageCheckEntity entity) {
        return ErrorPageResponse.builder()
                .url(entity.getUrl())
                .errorMessage(entity.getErrorMessage())
                .checkedAt(entity.getCheckedAt())
                .build();
    }

    private ChangedPageResponse toChangedPageResponse(PageCheckEntity entity) {
        return ChangedPageResponse.builder()
                .url(entity.getUrl())
                .previousH1(entity.getPreviousH1())
                .currentH1(entity.getCurrentH1())
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