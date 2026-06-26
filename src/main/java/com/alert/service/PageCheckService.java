package com.alert.service;

import com.alert.domain.dto.PageChangeType;
import com.alert.domain.dto.PageComparisonResult;
import com.alert.domain.entity.PageCheckEntity;
import com.alert.domain.entity.PageEntity;
import com.alert.repository.PageCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageCheckService {

    private final PageCheckRepository pageCheckRepository;

    public void logChange(PageEntity page, PageComparisonResult comparison) {
        comparison.changes().forEach(change ->
                pageCheckRepository.save(
                        PageCheckEntity.builder()
                                .pageId(page.getId())
                                .siteId(page.getSiteId())
                                .url(page.getUrl())
                                .changeType(change.type())
                                .oldValue(change.oldValue())
                                .newValue(change.newValue())
                                .build()
                )
        );
    }

    public void logAdded(PageEntity page) {
        pageCheckRepository.save(PageCheckEntity.builder()
                .pageId(page.getId())
                .siteId(page.getSiteId())
                .url(page.getUrl())
                .changeType(PageChangeType.PAGE_ADDED)
                .oldValue(null)
                .newValue(page.getCurrentH1())
                .build());
    }

    public void logDeleted(Long siteId, String url) {
        pageCheckRepository.save(PageCheckEntity.builder()
                .pageId(null)
                .siteId(siteId)
                .url(url)
                .changeType(PageChangeType.PAGE_DELETE)
                .oldValue(null)
                .newValue(null)
                .build());
    }
}