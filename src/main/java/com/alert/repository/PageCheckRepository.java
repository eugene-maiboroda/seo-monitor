package com.alert.repository;

import com.alert.domain.entity.PageCheckEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PageCheckRepository extends JpaRepository<PageCheckEntity, Long> {

        long countBySiteId(Long siteId);

        Page<PageCheckEntity> findBySiteIdOrderByCheckedAtDesc(Long siteId, Pageable pageable);
        Page<PageCheckEntity> findBySiteIdAndCheckedAtAfterOrderByCheckedAtDesc(Long siteId, LocalDateTime checkedAt, Pageable pageable);

}