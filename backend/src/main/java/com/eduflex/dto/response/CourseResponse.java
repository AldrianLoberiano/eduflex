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
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String category;
    private String level;
    private Integer duration;
    private Boolean isPublished;
    private Long instructorId;
    private String instructorName;
    private Integer enrollmentCount;
    private Integer lessonCount;
    private Integer quizCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
