package com.eduflex.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "enrollment_id", "lesson_id" })
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    private Integer watchedDuration; // in seconds for videos

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @LastModifiedDate
    private LocalDateTime lastAccessedAt;

    private LocalDateTime completedAt;
}
