package com.alert.domain.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record PageComparisonResult(
        boolean changed,
        List<PageChange> changes
) {

    public static PageComparisonResult noChanges() {
        return PageComparisonResult.builder()
                .changed(false)
                .changes(List.of())
                .build();
    }
}
