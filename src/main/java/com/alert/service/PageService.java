package com.alert.service;

import com.alert.domain.dto.PageAuditResult;
import com.alert.domain.dto.PageChange;
import com.alert.domain.dto.PageChangeType;
import com.alert.domain.dto.PageComparisonResult;
import com.alert.domain.entity.PageEntity;
import com.alert.domain.entity.SiteEntity;
import com.alert.parser.PageParser;
import com.alert.parser.SitemapParser;

import com.alert.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PageService {

    private final SitemapParser sitemapParser;
    private final PageParser pageParser;
    private final PageCheckService pageCheckService;

    private final PageRepository pageRepository;

    public List<PageComparisonResult> audit(SiteEntity site) {
        List<PageComparisonResult> results = new ArrayList<>();
        Set<String> urls = sitemapParser.getAllUrlFromSitemap(site.getUrl());

        for (String url : urls) {
            results.add(processPage(site, url));
        }

        return results;
    }

    private PageComparisonResult processPage(SiteEntity site, String url) {
        PageAuditResult result = pageParser.parsePage(url);

        Optional<PageEntity> pageOpt = pageRepository.findBySiteIdAndUrl(site.getId(), url);

        if (pageOpt.isEmpty()) {
            createNewPage(site, url, result);
            return PageComparisonResult.noChanges();
        }

        PageEntity page = pageOpt.get();
        PageComparisonResult comparison = compare(page, result);

        if (comparison.changed()) {
            pageCheckService.logChange(page, comparison);
            updateExistingPage(page, result);
        }

        return comparison;

    }


    private void createNewPage(SiteEntity site, String url, PageAuditResult result) {
        pageRepository.save(PageEntity.builder()
                .siteId(site.getId())
                .url(url)
                .currentH1(result.h1())
                .h1Exists(result.h1Exists())
                .redirected(result.redirected())
                .redirectUrl(result.redirectUrl())
                .errorMessage(result.error())
                .build());
    }

    private void updateExistingPage(PageEntity page, PageAuditResult result) {
        page.setCurrentH1(result.h1());
        page.setH1Exists(result.h1Exists());
        page.setRedirected(result.redirected());
        page.setRedirectUrl(result.redirectUrl());
        page.setErrorMessage(result.error());

        pageRepository.save(page);
    }

    private PageComparisonResult compare(PageEntity page, PageAuditResult result) {
        boolean h1Changed = !Objects.equals(page.getCurrentH1(), result.h1());
        boolean errorChanged = !Objects.equals(page.getErrorMessage(), result.error());
        boolean redirectChanged = !Objects.equals(page.getRedirectUrl(), result.redirectUrl());

        List<PageChange> changes = new ArrayList<>();

        if (h1Changed) {
            changes.add(new PageChange(
                    PageChangeType.H1_CHANGED,
                    page.getCurrentH1(),
                    result.h1()));
        }

        if (redirectChanged) {
            changes.add(new PageChange(
                    PageChangeType.REDIRECT_CHANGED,
                    page.getRedirectUrl(),
                    result.redirectUrl()));
        }

        if (errorChanged) {
            changes.add(new PageChange(
                    PageChangeType.ERROR_CHANGED,
                    page.getErrorMessage(),
                    result.error()));
        }

        return PageComparisonResult.builder()
                .changed(!changes.isEmpty())
                .changes(changes)
                .build();

    }
}
