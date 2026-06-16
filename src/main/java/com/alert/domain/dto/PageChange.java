package com.alert.domain.dto;

import lombok.Builder;

@Builder
public record PageChange(
        PageChangeType type,
        String oldValue,
        String newValue
) {
}
