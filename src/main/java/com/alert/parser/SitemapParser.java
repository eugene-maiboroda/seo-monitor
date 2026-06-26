package com.alert.parser;

import com.alert.client.HttpFetchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Service
@RequiredArgsConstructor
public class SitemapParser {

    private static final String LOC_SELECTOR = "loc";
    private static final String SITEMAP_SELECTOR = "sitemap";
    private static final String URL_SELECTOR = "url";

    private final HttpFetchClient httpFetchClient;

    public Set<String> getAllUrlFromSitemap(String siteUrl) {
        String normalizedUrl = normalizeUrl(siteUrl);
        String sitemapIndexUrl = normalizedUrl + "/sitemap_index.xml";

        Set<String> pages = new HashSet<>();

        for (String url : fetchSitemapIndex(sitemapIndexUrl, normalizedUrl)) {
            pages.addAll(fetchSitemapPage(url));
        }

        return pages;
    }

    private Set<String> fetchSitemapIndex(String sitemapIndexUrl, String sourceDomain) {
        try {
         String body =  httpFetchClient.fetchBody(sitemapIndexUrl).body();
         Document doc = Jsoup.parse(body);

            Set<String> maimUrls = new HashSet<>();
            for (Element sitemap : doc.select(SITEMAP_SELECTOR)) {
                String loc = getText(sitemap.selectFirst(LOC_SELECTOR));

                if (StringUtils.isBlank(loc) || loc.contains("/post")) {
                    continue;
                }
                maimUrls.add(loc);
            }

            log.debug("{} sitemap: index has {} child sitemaps", sourceDomain, maimUrls.size());
            return maimUrls;
        } catch (Exception e) {
            log.error("{} sitemap: failed to fetch index: {}", sourceDomain, e.getMessage());
            throw new RuntimeException("Failed to fetch sitemap index for: " + sourceDomain, e);
        }
    }

    private Set<String> fetchSitemapPage(String url) {
        String body =  httpFetchClient.fetchBody(url).body();
        Document doc = Jsoup.parse(body);

        Set<String> urls = new HashSet<>();
        for (Element urlElement : doc.select(URL_SELECTOR)) {
            String loc = getText(urlElement.selectFirst(LOC_SELECTOR));
            if (StringUtils.isBlank(loc)) {
                continue;
            }
            urls.add(loc);

        }
        return urls;

    }

    public static String getText(Element element) {
        return element != null ? element.text().trim() : EMPTY;
    }

    private String normalizeUrl(String url) {
        return url.endsWith("/")
                ? url.substring(0, url.length() - 1)
                : url;
    }
}
