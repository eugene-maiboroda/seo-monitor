package com.alert.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "sites")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "site_id_seq", initialValue = 10000, allocationSize = 1)
    private Long id;

    private String url;

    private String name;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private int checkIntervalHours = 6;

    private LocalDateTime lastCheckAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
