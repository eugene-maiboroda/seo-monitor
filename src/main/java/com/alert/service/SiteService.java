package com.alert.service;

import com.alert.domain.dto.SiteRequestDto;
import com.alert.domain.dto.SiteResponseDto;
import com.alert.domain.entity.SiteEntity;
import com.alert.domain.exeption.SiteNotFoundException;
import com.alert.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteResponseDto create(SiteRequestDto request) {
        SiteEntity siteSaved = siteRepository.save(mapToEntity(request));
        return mapToResponse(siteSaved);
    }

    public List<SiteResponseDto> findAll() {
        return siteRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SiteResponseDto findById(Long id) {
        SiteEntity siteEntity = siteRepository.findById(id)
                .orElseThrow(() -> new SiteNotFoundException("Site not found"));
        return mapToResponse(siteEntity);
    }

    public void delete(Long id) {
        if (!siteRepository.existsById(id)) {
            throw new SiteNotFoundException("Site not found");
        }

        siteRepository.deleteById(id);
    }

    public void update(Long id, SiteRequestDto request) {
        SiteEntity siteEntity = siteRepository.findById(id)
                .orElseThrow(() -> new SiteNotFoundException("Site not found"));
        siteEntity.setName(request.name());
        siteEntity.setUrl(request.url());
        siteEntity.setCheckIntervalHours(request.intervalHours());
        siteRepository.save(siteEntity);
    }

    private SiteEntity mapToEntity(SiteRequestDto request) {
        return SiteEntity.builder()
                .name(request.name())
                .checkIntervalHours(request.intervalHours())
                .url(request.url())
                .build();

    }

    private SiteResponseDto mapToResponse(SiteEntity savedEntity) {
        return SiteResponseDto.builder()
                .id(savedEntity.getId())
                .url(savedEntity.getUrl())
                .name(savedEntity.getName())
                .build();
    }
}
