package com.alert.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SummaryResponse(
        long pages,
        long noH1Page,
        long redirectPage,
        long errorPage,
        long changesPage,
        LocalDateTime lastAudit
) {
}
