package com.eduflex.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String thumbnailUrl;

    @NotBlank(message = "Category is required")
    private String category;

    private String level;

    private Integer duration;

    @NotNull(message = "Published status is required")
    private Boolean isPublished;
}
