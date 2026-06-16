package com.alert.controller;

import com.alert.domain.dto.SiteRequestDto;
import com.alert.domain.dto.SiteResponseDto;
import com.alert.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    public SiteResponseDto addSite(SiteRequestDto request) {
        return siteService.create(request);
    }

    @GetMapping
    public List<SiteResponseDto> findAll() {
        return siteService.findAll();
    }

    @GetMapping("/{id}")
    public SiteResponseDto findById(@PathVariable Long id) {
        return siteService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        siteService.delete(id);
        return ResponseEntity.ok("Site deleted");
    }

    @PatchMapping
    public ResponseEntity<String> update(@RequestParam Long id, SiteRequestDto request) {
        siteService.update(id, request);
        return ResponseEntity.ok("Site updated");
    }
}
