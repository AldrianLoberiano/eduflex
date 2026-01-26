package com.eduflex.service;

import com.eduflex.dto.request.QuizRequest;
import com.eduflex.dto.request.QuizSubmissionRequest;
import com.eduflex.dto.response.QuizAttemptResponse;
import com.eduflex.dto.response.QuizResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.*;
import com.eduflex.model.enums.UserRole;
import com.eduflex.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request, Long instructorId) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new BadRequestException("You are not the instructor of this course");
        }

        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .course(course)
                .timeLimit(request.getTimeLimit())
                .passingScore(request.getPassingScore())
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .orderIndex(request.getOrderIndex())
                .questions(new ArrayList<>())
                .build();

        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
            quiz.setLesson(lesson);
        }

        // Add questions
        if (request.getQuestions() != null) {
            for (QuizRequest.QuestionRequest qReq : request.getQuestions()) {
                QuizQuestion question = QuizQuestion.builder()
                        .quiz(quiz)
                        .question(qReq.getQuestion())
                        .type(qReq.getType())
                        .points(qReq.getPoints())
                        .orderIndex(qReq.getOrderIndex())
                        .options(new ArrayList<>())
                        .build();

                // Add options
                if (qReq.getOptions() != null) {
                    for (QuizRequest.OptionRequest oReq : qReq.getOptions()) {
                        QuizOption option = QuizOption.builder()
                                .question(question)
                                .optionText(oReq.getOptionText())
                                .isCorrect(oReq.getIsCorrect())
                                .orderIndex(oReq.getOrderIndex())
                                .build();
                        question.getOptions().add(option);
                    }
                }

                quiz.getQuestions().add(question);
            }
        }

        quiz = quizRepository.save(quiz);
        return mapToResponse(quiz, true);
    }

    @Transactional
    public QuizResponse updateQuiz(Long quizId, QuizRequest request, Long instructorId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        if (!quiz.getCourse().getInstructor().getId().equals(instructorId)) {
            throw new BadRequestException("You are not the instructor of this course");
        }

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setTimeLimit(request.getTimeLimit());
        quiz.setPassingScore(request.getPassingScore());
        quiz.setIsPublished(request.getIsPublished());
        quiz.setOrderIndex(request.getOrderIndex());

        // Update questions (simple approach: clear and recreate)
        quiz.getQuestions().clear();

        if (request.getQuestions() != null) {
            for (QuizRequest.QuestionRequest qReq : request.getQuestions()) {
                QuizQuestion question = QuizQuestion.builder()
                        .quiz(quiz)
                        .question(qReq.getQuestion())
                        .type(qReq.getType())
                        .points(qReq.getPoints())
                        .orderIndex(qReq.getOrderIndex())
                        .options(new ArrayList<>())
                        .build();

                if (qReq.getOptions() != null) {
                    for (QuizRequest.OptionRequest oReq : qReq.getOptions()) {
                        QuizOption option = QuizOption.builder()
                                .question(question)
                                .optionText(oReq.getOptionText())
                                .isCorrect(oReq.getIsCorrect())
                                .orderIndex(oReq.getOrderIndex())
                                .build();
                        question.getOptions().add(option);
                    }
                }

                quiz.getQuestions().add(question);
            }
        }

        quiz = quizRepository.save(quiz);
        return mapToResponse(quiz, true);
    }

    public void deleteQuiz(Long quizId, Long instructorId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        if (!quiz.getCourse().getInstructor().getId().equals(instructorId)) {
            throw new BadRequestException("You are not the instructor of this course");
        }

        quizRepository.delete(quiz);
    }

    public List<QuizResponse> getQuizzesByCourse(Long courseId, Long userId, UserRole userRole) {
        List<Quiz> quizzes;

        if (userRole == UserRole.STUDENT) {
            quizzes = quizRepository.findByCourseIdAndIsPublishedTrue(courseId);
            return quizzes.stream()
                    .map(q -> mapToResponse(q, false)) // Hide correct answers
                    .collect(Collectors.toList());
        } else {
            quizzes = quizRepository.findByCourseId(courseId);
            return quizzes.stream()
                    .map(q -> mapToResponse(q, true)) // Show correct answers
                    .collect(Collectors.toList());
        }
    }

    public QuizResponse getQuizById(Long quizId, Long userId, UserRole userRole) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        boolean includeAnswers = userRole != UserRole.STUDENT;
        return mapToResponse(quiz, includeAnswers);
    }

    @Transactional
    public QuizAttemptResponse submitQuiz(QuizSubmissionRequest request, Long studentId) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Create attempt
        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .startedAt(LocalDateTime.now())
                .totalPoints(0)
                .earnedPoints(0)
                .score(0)
                .passed(false)
                .answers(new ArrayList<>())
                .build();

        int totalPoints = 0;
        int earnedPoints = 0;

        // Grade answers
        for (QuizSubmissionRequest.AnswerRequest answerReq : request.getAnswers()) {
            QuizQuestion question = quiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(answerReq.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

            totalPoints += question.getPoints();

            QuizOption selectedOption = null;
            boolean isCorrect = false;
            int pointsEarned = 0;

            if (answerReq.getSelectedOptionId() != null) {
                selectedOption = question.getOptions().stream()
                        .filter(o -> o.getId().equals(answerReq.getSelectedOptionId()))
                        .findFirst()
                        .orElse(null);

                if (selectedOption != null && selectedOption.getIsCorrect()) {
                    isCorrect = true;
                    pointsEarned = question.getPoints();
                    earnedPoints += pointsEarned;
                }
            }

            QuizAnswer answer = QuizAnswer.builder()
                    .attempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .textAnswer(answerReq.getTextAnswer())
                    .isCorrect(isCorrect)
                    .pointsEarned(pointsEarned)
                    .build();

            attempt.getAnswers().add(answer);
        }

        attempt.setTotalPoints(totalPoints);
        attempt.setEarnedPoints(earnedPoints);
        attempt.setScore(totalPoints > 0 ? (earnedPoints * 100) / totalPoints : 0);
        attempt.setPassed(attempt.getScore() >= quiz.getPassingScore());
        attempt.setCompletedAt(LocalDateTime.now());

        attempt = quizAttemptRepository.save(attempt);
        return mapAttemptToResponse(attempt);
    }

    public List<QuizAttemptResponse> getStudentAttempts(Long studentId) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByStudentIdOrderByStartedAtDesc(studentId);
        return attempts.stream()
                .map(this::mapAttemptToResponse)
                .collect(Collectors.toList());
    }

    public List<QuizAttemptResponse> getQuizAttempts(Long quizId) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByQuizId(quizId);
        return attempts.stream()
                .map(this::mapAttemptToResponse)
                .collect(Collectors.toList());
    }

    private QuizResponse mapToResponse(Quiz quiz, boolean includeAnswers) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .courseId(quiz.getCourse().getId())
                .courseName(quiz.getCourse().getTitle())
                .lessonId(quiz.getLesson() != null ? quiz.getLesson().getId() : null)
                .timeLimit(quiz.getTimeLimit())
                .passingScore(quiz.getPassingScore())
                .isPublished(quiz.getIsPublished())
                .orderIndex(quiz.getOrderIndex())
                .totalQuestions(quiz.getQuestions().size())
                .totalPoints(quiz.getQuestions().stream().mapToInt(QuizQuestion::getPoints).sum())
                .questions(quiz.getQuestions().stream()
                        .map(q -> mapQuestionToResponse(q, includeAnswers))
                        .collect(Collectors.toList()))
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .build();
    }

    private QuizResponse.QuestionResponse mapQuestionToResponse(QuizQuestion question, boolean includeAnswers) {
        return QuizResponse.QuestionResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .type(question.getType())
                .points(question.getPoints())
                .orderIndex(question.getOrderIndex())
                .options(question.getOptions().stream()
                        .map(o -> QuizResponse.OptionResponse.builder()
                                .id(o.getId())
                                .optionText(o.getOptionText())
                                .isCorrect(includeAnswers ? o.getIsCorrect() : null)
                                .orderIndex(o.getOrderIndex())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private QuizAttemptResponse mapAttemptToResponse(QuizAttempt attempt) {
        return QuizAttemptResponse.builder()
                .id(attempt.getId())
                .quizId(attempt.getQuiz().getId())
                .quizTitle(attempt.getQuiz().getTitle())
                .studentId(attempt.getStudent().getId())
                .studentName(attempt.getStudent().getFullName())
                .startedAt(attempt.getStartedAt())
                .completedAt(attempt.getCompletedAt())
                .score(attempt.getScore())
                .totalPoints(attempt.getTotalPoints())
                .earnedPoints(attempt.getEarnedPoints())
                .passed(attempt.getPassed())
                .answers(attempt.getAnswers().stream()
                        .map(this::mapAnswerToResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private QuizAttemptResponse.AnswerResponse mapAnswerToResponse(QuizAnswer answer) {
        String correctAnswer = answer.getQuestion().getOptions().stream()
                .filter(QuizOption::getIsCorrect)
                .map(QuizOption::getOptionText)
                .collect(Collectors.joining(", "));

        return QuizAttemptResponse.AnswerResponse.builder()
                .questionId(answer.getQuestion().getId())
                .question(answer.getQuestion().getQuestion())
                .selectedOptionId(answer.getSelectedOption() != null ? answer.getSelectedOption().getId() : null)
                .selectedOptionText(
                        answer.getSelectedOption() != null ? answer.getSelectedOption().getOptionText() : null)
                .textAnswer(answer.getTextAnswer())
                .isCorrect(answer.getIsCorrect())
                .pointsEarned(answer.getPointsEarned())
                .maxPoints(answer.getQuestion().getPoints())
                .correctAnswer(correctAnswer)
                .build();
    }
}
