package com.alert.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_sequence_generator")
    @SequenceGenerator(name = "page_sequence_generator", sequenceName = "page_id_seq", initialValue = 10000, allocationSize = 1)
    private Long id;

    private Long siteId;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(length = 1000)
    private String currentH1;

    private boolean h1Exists;

    @UpdateTimestamp
    private LocalDateTime lastCheckedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}