package com.alert.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_checks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "site_check_id_seq", initialValue = 10000, allocationSize = 1)
    private Long id;

    private Long siteId;

    private boolean siteAvailable;
    private boolean sitemapExists;
    private boolean robotsExists;
    private boolean pageAuditPassed;

    @Enumerated(EnumType.STRING)
    private CheckStatus status;

    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime checkedAt;
}
