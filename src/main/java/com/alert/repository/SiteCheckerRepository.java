package com.alert.repository;

import com.alert.domain.entity.SiteCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteCheckerRepository extends JpaRepository<SiteCheckEntity, Long> {

}
