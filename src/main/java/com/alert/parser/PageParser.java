package com.alert.parser;

import com.alert.client.HttpFetchClient;
import com.alert.domain.dto.FetchResult;
import com.alert.domain.dto.PageAuditResult;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@RequiredArgsConstructor
public class PageParser {

    private final HttpFetchClient fetchClient;

    public PageAuditResult parsePage(String url) {
        boolean redirected = false;
        String redirectUrl = null;

        try {
            FetchResult fetchResult = fetchClient.fetchBody(url);
            if (fetchResult.isRedirect()) {
                if (fetchResult.redirectUrl() != null) {
                    redirected = true;
                    redirectUrl = fetchResult.redirectUrl();
                    fetchResult = fetchClient.fetchBody(redirectUrl);
                }
            }

            String body = fetchResult.body();
            if (body== null || body.isBlank()) {
                throw new IllegalStateException("Response body is empty");
            }

            Document doc = Jsoup.parse(body);
            String h1 = getText(doc.selectFirst("h1"));

            return PageAuditResult.builder()
                    .url(url)
                    .h1(h1)
                    .h1Exists(!h1.isBlank())
                    .redirected(redirected)
                    .redirectUrl(redirectUrl)
                    .error(null)
                    .build();

        } catch (Exception e) {

            return PageAuditResult.builder()
                    .url(url)
                    .h1(null)
                    .h1Exists(false)
                    .redirected(redirected)
                    .redirectUrl(redirectUrl)
                    .error(e.getMessage())
                    .build();
        }
    }

    public static String getText(Element element) {
        return element != null ? element.text().trim() : EMPTY;
    }
}
