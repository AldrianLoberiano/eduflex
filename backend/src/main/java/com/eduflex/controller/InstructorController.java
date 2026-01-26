package com.eduflex.controller;

import com.eduflex.dto.request.CourseRequest;
import com.eduflex.dto.request.LessonRequest;
import com.eduflex.dto.response.ApiResponse;
import com.eduflex.dto.response.CourseResponse;
import com.eduflex.dto.response.EnrollmentResponse;
import com.eduflex.dto.response.LessonResponse;
import com.eduflex.security.CustomUserDetailsService;
import com.eduflex.service.CourseService;
import com.eduflex.service.EnrollmentService;
import com.eduflex.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public class InstructorController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final EnrollmentService enrollmentService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses(Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        List<CourseResponse> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        CourseResponse course = courseService.createCourse(request, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", course));
    }

    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        LessonResponse lesson = lessonService.createLesson(courseId, request, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lesson created successfully", lesson));
    }

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getCourseLessons(
            @PathVariable Long courseId,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        List<LessonResponse> lessons = lessonService.getLessonsByCourseForInstructor(courseId, instructorId);
        return ResponseEntity.ok(ApiResponse.success(lessons));
    }

    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        LessonResponse lesson = lessonService.updateLesson(lessonId, request, instructorId);
        return ResponseEntity.ok(ApiResponse.success("Lesson updated successfully", lesson));
    }

    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(
            @PathVariable Long lessonId,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        lessonService.deleteLesson(lessonId, instructorId);
        return ResponseEntity.ok(ApiResponse.success("Lesson deleted successfully", null));
    }

    @GetMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getCourseEnrollments(
            @PathVariable Long courseId) {
        List<EnrollmentResponse> enrollments = enrollmentService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
}
