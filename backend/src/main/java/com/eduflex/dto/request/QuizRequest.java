package com.eduflex.dto.request;

import com.eduflex.model.enums.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {
    private String title;
    private String description;
    private Long courseId;
    private Long lessonId;
    private Integer timeLimit;
    private Integer passingScore;
    private Boolean isPublished;
    private Integer orderIndex;
    private List<QuestionRequest> questions;

    @Data
    public static class QuestionRequest {
        private String question;
        private QuestionType type;
        private Integer points;
        private Integer orderIndex;
        private List<OptionRequest> options;
    }

    @Data
    public static class OptionRequest {
        private String optionText;
        private Boolean isCorrect;
        private Integer orderIndex;
    }
}
