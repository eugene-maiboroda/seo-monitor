package com.alert.parser;

import com.alert.domain.dto.SiteAuditResult;

public interface SiteHealthChecker {

    SiteAuditResult audit(String url);

}
