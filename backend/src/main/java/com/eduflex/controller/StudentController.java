package com.eduflex.controller;

import com.eduflex.dto.response.ApiResponse;
import com.eduflex.dto.response.EnrollmentResponse;
import com.eduflex.dto.response.LessonResponse;
import com.eduflex.dto.response.ProgressResponse;
import com.eduflex.security.CustomUserDetailsService;
import com.eduflex.service.EnrollmentService;
import com.eduflex.service.LessonService;
import com.eduflex.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;
    private final LessonService lessonService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollInCourse(
            @PathVariable Long courseId,
            Authentication authentication) {
        Long studentId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        EnrollmentResponse enrollment = enrollmentService.enrollInCourse(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Successfully enrolled in course", enrollment));
    }

    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments(
            Authentication authentication) {
        Long studentId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        List<EnrollmentResponse> enrollments = enrollmentService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }

    @DeleteMapping("/enrollments/{enrollmentId}")
    public ResponseEntity<ApiResponse<Void>> unenroll(
            @PathVariable Long enrollmentId,
            Authentication authentication) {
        Long studentId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        enrollmentService.unenroll(enrollmentId, studentId);
        return ResponseEntity.ok(ApiResponse.success("Unenrolled successfully", null));
    }

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getCourseLessons(@PathVariable Long courseId) {
        List<LessonResponse> lessons = lessonService.getPublishedLessonsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(lessons));
    }

    @GetMapping("/enrollments/{enrollmentId}/progress")
    public ResponseEntity<ApiResponse<List<ProgressResponse>>> getEnrollmentProgress(
            @PathVariable Long enrollmentId) {
        List<ProgressResponse> progress = progressService.getEnrollmentProgress(enrollmentId);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @PostMapping("/enrollments/{enrollmentId}/lessons/{lessonId}/progress")
    public ResponseEntity<ApiResponse<ProgressResponse>> trackLessonProgress(
            @PathVariable Long enrollmentId,
            @PathVariable Long lessonId,
            @RequestParam(required = false) Integer watchedDuration) {
        ProgressResponse progress = progressService.trackProgress(enrollmentId, lessonId, watchedDuration);
        return ResponseEntity.ok(ApiResponse.success("Progress tracked", progress));
    }

    @PostMapping("/enrollments/{enrollmentId}/lessons/{lessonId}/complete")
    public ResponseEntity<ApiResponse<ProgressResponse>> markLessonComplete(
            @PathVariable Long enrollmentId,
            @PathVariable Long lessonId) {
        ProgressResponse progress = progressService.markLessonComplete(enrollmentId, lessonId);
        return ResponseEntity.ok(ApiResponse.success("Lesson marked as complete", progress));
    }
}
