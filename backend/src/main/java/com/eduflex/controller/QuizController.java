package com.eduflex.controller;

import com.eduflex.dto.request.QuizRequest;
import com.eduflex.dto.request.QuizSubmissionRequest;
import com.eduflex.dto.response.ApiResponse;
import com.eduflex.dto.response.QuizAttemptResponse;
import com.eduflex.dto.response.QuizResponse;
import com.eduflex.model.enums.UserRole;
import com.eduflex.security.JwtUtil;
import com.eduflex.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @RequestBody QuizRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        QuizResponse quiz = quizService.createQuiz(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Quiz created successfully", quiz));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> updateQuiz(
            @PathVariable Long id,
            @RequestBody QuizRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        QuizResponse quiz = quizService.updateQuiz(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Quiz updated successfully", quiz));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        quizService.deleteQuiz(id, userId);
        return ResponseEntity.ok(ApiResponse.<Void>success("Quiz deleted successfully", null));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getQuizzesByCourse(
            @PathVariable Long courseId,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        UserRole userRole = getUserRoleFromToken(httpRequest);
        List<QuizResponse> quizzes = quizService.getQuizzesByCourse(courseId, userId, userRole);
        return ResponseEntity.ok(ApiResponse.success("Quizzes retrieved successfully", quizzes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getQuizById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        UserRole userRole = getUserRoleFromToken(httpRequest);
        QuizResponse quiz = quizService.getQuizById(id, userId, userRole);
        return ResponseEntity.ok(ApiResponse.success("Quiz retrieved successfully", quiz));
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<QuizAttemptResponse>> submitQuiz(
            @RequestBody QuizSubmissionRequest request,
            HttpServletRequest httpRequest) {
        Long studentId = getUserIdFromToken(httpRequest);
        QuizAttemptResponse result = quizService.submitQuiz(request, studentId);
        return ResponseEntity.ok(ApiResponse.success("Quiz submitted successfully", result));
    }

    @GetMapping("/attempts/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>> getMyAttempts(
            HttpServletRequest httpRequest) {
        Long studentId = getUserIdFromToken(httpRequest);
        List<QuizAttemptResponse> attempts = quizService.getStudentAttempts(studentId);
        return ResponseEntity.ok(ApiResponse.success("Attempts retrieved successfully", attempts));
    }

    @GetMapping("/{quizId}/attempts")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>> getQuizAttempts(
            @PathVariable Long quizId) {
        List<QuizAttemptResponse> attempts = quizService.getQuizAttempts(quizId);
        return ResponseEntity.ok(ApiResponse.success("Attempts retrieved successfully", attempts));
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtUtil.extractUserId(token);
    }

    private UserRole getUserRoleFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
        return UserRole.valueOf(role);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("No token provided");
    }
}
