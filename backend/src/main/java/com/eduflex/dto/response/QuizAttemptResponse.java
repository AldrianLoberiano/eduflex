package com.eduflex.dto.response;

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
public class QuizAttemptResponse {
    private Long id;
    private Long quizId;
    private String quizTitle;
    private Long studentId;
    private String studentName;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer score;
    private Integer totalPoints;
    private Integer earnedPoints;
    private Boolean passed;
    private List<AnswerResponse> answers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerResponse {
        private Long questionId;
        private String question;
        private Long selectedOptionId;
        private String selectedOptionText;
        private String textAnswer;
        private Boolean isCorrect;
        private Integer pointsEarned;
        private Integer maxPoints;
        private String correctAnswer; // Only shown after completion
    }
}
