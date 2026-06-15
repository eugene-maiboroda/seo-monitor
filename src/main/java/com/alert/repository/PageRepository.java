package com.alert.repository;

import com.alert.domain.entity.PageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {

    Optional <PageEntity> findBySiteIdAndUrl(Long siteId ,String url);

    long countBySiteId(Long siteId);
    long countBySiteIdAndH1ExistsFalse(Long siteId);
    List<PageEntity> findBySiteIdAndH1ExistsFalse(Long siteId);
    Page<PageEntity> findBySiteId(Long siteId, Pageable pageable);
}
