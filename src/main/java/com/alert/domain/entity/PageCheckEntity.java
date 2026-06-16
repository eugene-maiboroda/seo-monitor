package com.alert.domain.entity;

import com.alert.domain.dto.PageChangeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "page_checks")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_check_sequence_generator")
    @SequenceGenerator(name = "page_check_sequence_generator", sequenceName = "page_check_id_seq", initialValue = 10000, allocationSize = 1)
    private Long id;

    private Long pageId;
    private Long siteId;

    private String url;

    @Enumerated(EnumType.STRING)
    private PageChangeType changeType;

    private String oldValue;

    private String newValue;

    @CreationTimestamp
    private LocalDateTime checkedAt;

}

