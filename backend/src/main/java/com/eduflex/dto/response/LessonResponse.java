package com.eduflex.dto.response;

import com.eduflex.model.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private LessonType type;
    private String contentUrl;
    private Integer duration;
    private Integer orderIndex;
    private Boolean isPublished;
    private Long courseId;
    private LocalDateTime createdAt;
}
