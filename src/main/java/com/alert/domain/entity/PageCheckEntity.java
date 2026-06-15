package com.alert.domain.entity;

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

    private String url;

    private Long pageId;

    private Long siteId;

    @Column(length = 1000)
    private String previousH1;

    @Column(length = 1000)
    private String currentH1;

    private boolean h1Exists;

    private boolean redirected;

    private String redirectUrl;

    private boolean h1Changed;

    @Column(length = 1000)
    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime checkedAt;

}

