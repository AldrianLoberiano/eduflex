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
public class ProgressResponse {
    private Long id;
    private Long lessonId;
    private String lessonTitle;
    private Boolean completed;
    private Integer watchedDuration;
    private LocalDateTime startedAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime completedAt;
}
