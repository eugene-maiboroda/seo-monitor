package com.alert.service;

import com.alert.domain.dto.PageAuditResult;
import com.alert.domain.entity.PageCheckEntity;
import com.alert.domain.entity.PageEntity;
import com.alert.domain.entity.SiteEntity;
import com.alert.parser.PageParser;
import com.alert.parser.SitemapParser;
import com.alert.repository.PageCheckRepository;
import com.alert.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PageAuditService {

    private final SitemapParser sitemapParser;
    private final PageParser pageParser;

    private final PageRepository pageRepository;
    private final PageCheckRepository pageCheckRepository;

    public boolean audit(SiteEntity site) {
        boolean auditPassed = true;
        Set<String> urls = sitemapParser.getAllUrlFromSitemap(site.getUrl());

        for (String url : urls) {
            if (!processPage(site, url)) {
                auditPassed = false;
            }
        }

        return auditPassed;
    }

    private boolean processPage(SiteEntity site, String url) {
        PageAuditResult result = pageParser.parsePage(url);

        if (result.error() != null) {
            handleError(site, url, result.error());
            return false;
        }

        Optional<PageEntity> page = pageRepository.findBySiteIdAndUrl(site.getId(), url);
        return page.map(pageEntity -> updateExistingPage(pageEntity, result))
                .orElseGet(() -> createNewPage(site, url, result));

    }


    private boolean createNewPage(SiteEntity site, String url, PageAuditResult result) {
        PageEntity savedPage = pageRepository.save(PageEntity.builder()
                .siteId(site.getId())
                .url(url)
                .currentH1(result.h1())
                .h1Exists(result.h1Exists())
                .build());

        pageCheckRepository.save(PageCheckEntity.builder()
                .pageId(savedPage.getId())
                .siteId(site.getId())
                .url(url)
                .previousH1(null)
                .currentH1(result.h1())
                .h1Changed(false)
                .h1Exists(result.h1Exists())
                .redirected(result.redirected())
                .redirectUrl(result.redirectUrl())
                .build()

        );
        return result.h1Exists();
    }

    private boolean updateExistingPage(PageEntity page, PageAuditResult result) {
        boolean h1Changed = !Objects.equals(page.getCurrentH1(), result.h1());
        boolean hasError = result.error() != null;
        boolean redirected = result.redirected();

        if (h1Changed || hasError || redirected) {
            pageCheckRepository.save(PageCheckEntity.builder()
                    .pageId(page.getId())
                    .siteId(page.getSiteId())
                    .url(page.getUrl())
                    .previousH1(page.getCurrentH1())
                    .currentH1(result.h1())
                    .h1Changed(h1Changed)
                    .h1Exists(result.h1Exists())
                    .redirected(result.redirected())
                    .redirectUrl(result.redirectUrl())
                    .build()
            );

            page.setCurrentH1(result.h1());
            page.setH1Exists(result.h1Exists());
            pageRepository.save(page);
        }

        return !h1Changed || !hasError;
    }

    private void handleError(SiteEntity site, String url, String errorMessage) {
        PageEntity page = pageRepository.findBySiteIdAndUrl(site.getId(), url)
                .orElseGet(() -> pageRepository.save(PageEntity.builder()
                        .siteId(site.getId())
                        .url(url)
                        .build()));

        pageCheckRepository.save(PageCheckEntity.builder()
                .pageId(page.getId())
                .errorMessage(errorMessage)
                .build()
        );
    }
}

