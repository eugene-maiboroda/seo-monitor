package com.alert.repository;

import com.alert.domain.entity.PageCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PageCheckRepository extends JpaRepository<PageCheckEntity, Long> {

    long countByRedirectedTrue(Long siteId);
    long countBySiteIdAndH1ChangedTrueAndCheckedAtAfter(Long siteId, LocalDateTime dateTime);
    long countBySiteIdAndErrorMessageIsNotNullAndCheckedAtAfter(Long siteId, LocalDateTime dateTime);

    List<PageCheckEntity> findBySiteIdAndRedirectedTrue(Long siteId);
    List<PageCheckEntity> findBySiteIdAndErrorMessageIsNotNullAndCheckedAtAfter(Long siteId, LocalDateTime dateTime);
    List<PageCheckEntity> findBySiteIdAndH1ChangedTrueAndCheckedAtAfter(Long siteId, LocalDateTime dateTime);
}