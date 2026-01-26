package com.eduflex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private String courseCategory;
    private Integer progressPercentage;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private Boolean isCompleted;
}
