package com.alert.controller;

import com.alert.controller.dto.*;
import com.alert.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{siteId}/summary")
    public SummaryResponse getSummary(@PathVariable Long siteId) {
        return dashboardService.getSummary(siteId);
    }

    @GetMapping("/{siteId}/pages")
    public Page<PageResponse> getPages(@PathVariable Long siteId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size
    ) {
        return dashboardService.getPages(siteId, page, size);
    }


    @GetMapping("{siteId}/no-h1")
    public List<NoH1PageResponse> getNoH1(@PathVariable Long siteId) {
        return dashboardService.getNoH1(siteId);
    }

    @GetMapping("{siteId}/redirects")
    public List<RedirectPageResponse> getRedirects(@PathVariable Long siteId) {
        return dashboardService.getRedirects(siteId);
    }

    @GetMapping("{siteId}/errors")
    public List<ErrorPageResponse> getErrors(@PathVariable Long siteId) {
        return dashboardService.getErrors(siteId);
    }

    @GetMapping("{siteId}/changes")
    public Page<ChangedPageResponse> getChanges(
            @PathVariable Long siteId,
            @RequestParam(required = false) Integer days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size

    ) {
        return dashboardService.getChanges(siteId, days, page, size);
    }
}