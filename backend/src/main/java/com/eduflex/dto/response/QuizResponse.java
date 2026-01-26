package com.eduflex.dto.response;

import com.eduflex.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private String courseName;
    private Long lessonId;
    private Integer timeLimit;
    private Integer passingScore;
    private Boolean isPublished;
    private Integer orderIndex;
    private Integer totalQuestions;
    private Integer totalPoints;
    private List<QuestionResponse> questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResponse {
        private Long id;
        private String question;
        private QuestionType type;
        private Integer points;
        private Integer orderIndex;
        private List<OptionResponse> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionResponse {
        private Long id;
        private String optionText;
        private Boolean isCorrect; // Only included for instructors
        private Integer orderIndex;
    }
}
