package com.eduflex.controller;

import com.eduflex.dto.request.CourseRequest;
import com.eduflex.dto.response.ApiResponse;
import com.eduflex.dto.response.CourseResponse;
import com.eduflex.security.CustomUserDetailsService;
import com.eduflex.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/published")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getPublishedCourses() {
        List<CourseResponse> courses = courseService.getPublishedCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCoursesByCategory(@PathVariable String category) {
        List<CourseResponse> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        CourseResponse course = courseService.createCourse(request, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        CourseResponse course = courseService.updateCourse(id, request, instructorId);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id,
            Authentication authentication) {
        Long instructorId = userDetailsService.getUserByEmail(authentication.getName()).getId();
        courseService.deleteCourse(id, instructorId);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
    }
}
