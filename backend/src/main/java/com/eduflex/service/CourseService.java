package com.eduflex.service;

import com.eduflex.dto.request.CourseRequest;
import com.eduflex.dto.response.CourseResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.Course;
import com.eduflex.model.entity.User;
import com.eduflex.model.enums.UserRole;
import com.eduflex.repository.CourseRepository;
import com.eduflex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseResponse createCourse(CourseRequest request, Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        if (instructor.getRole() != UserRole.INSTRUCTOR && instructor.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("Only instructors can create courses");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .category(request.getCategory())
                .level(request.getLevel())
                .duration(request.getDuration())
                .isPublished(request.getIsPublished())
                .instructor(instructor)
                .build();

        course = courseRepository.save(course);
        return mapToCourseResponse(course);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return mapToCourseResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getPublishedCourses() {
        return courseRepository.findByIsPublishedTrue().stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseResponse updateCourse(Long courseId, CourseRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Check if user is the course instructor or admin
        if (!course.getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to update this course");
            }
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        course.setDuration(request.getDuration());
        course.setIsPublished(request.getIsPublished());

        course = courseRepository.save(course);
        return mapToCourseResponse(course);
    }

    @Transactional
    public void deleteCourse(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Check if user is the course instructor or admin
        if (!course.getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to delete this course");
            }
        }

        courseRepository.delete(course);
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnailUrl(course.getThumbnailUrl())
                .category(course.getCategory())
                .level(course.getLevel())
                .duration(course.getDuration())
                .isPublished(course.getIsPublished())
                .instructorId(course.getInstructor().getId())
                .instructorName(course.getInstructor().getFullName())
                .enrollmentCount(course.getEnrollmentCount())
                .lessonCount(course.getLessonCount())
                .quizCount(course.getQuizCount())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
