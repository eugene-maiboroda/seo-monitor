package com.alert.parser;

import com.alert.client.HttpFetchClient;
import com.alert.domain.dto.SiteAuditResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpSiteHealthChecker implements SiteHealthChecker {

    private static final String SITE_MAP_URL = "/sitemap.xml";
    private static final String ROBOTS_TXT_URL = "/robots.txt";

    private final HttpFetchClient fetchClient;

    @Override
    public SiteAuditResult audit(String url) {
        try {
            String normalizedUrl = normalizeUrl(url);

            boolean sitemapExists = fetchClient.exists(normalizedUrl + SITE_MAP_URL);
            boolean robotsExists = fetchClient.exists(normalizedUrl + ROBOTS_TXT_URL);

            return SiteAuditResult.builder()
                    .siteAvailable(true)
                    .sitemapExists(sitemapExists)
                    .robotsExists(robotsExists)
                    .error(null)
                    .build();

        } catch (Exception e) {
            return SiteAuditResult.builder()
                    .siteAvailable(false)
                    .sitemapExists(false)
                    .robotsExists(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    private String normalizeUrl(String url) {
        return url.endsWith("/")
                ? url.substring(0, url.length() - 1)
                : url;
    }

}
