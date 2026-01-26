package com.eduflex.dto.request;

import com.eduflex.model.enums.LessonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Lesson type is required")
    private LessonType type;

    private String contentUrl;

    private Integer duration;

    @NotNull(message = "Order index is required")
    private Integer orderIndex;

    @NotNull(message = "Published status is required")
    private Boolean isPublished;
}
