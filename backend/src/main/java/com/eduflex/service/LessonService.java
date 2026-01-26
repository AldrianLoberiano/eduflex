package com.eduflex.service;

import com.eduflex.dto.request.LessonRequest;
import com.eduflex.dto.response.LessonResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.Course;
import com.eduflex.model.entity.Lesson;
import com.eduflex.model.entity.User;
import com.eduflex.model.enums.UserRole;
import com.eduflex.repository.CourseRepository;
import com.eduflex.repository.LessonRepository;
import com.eduflex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public LessonResponse createLesson(Long courseId, LessonRequest request, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Verify instructor owns the course
        if (!course.getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to add lessons to this course");
            }
        }

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .contentUrl(request.getContentUrl())
                .duration(request.getDuration())
                .orderIndex(request.getOrderIndex())
                .isPublished(request.getIsPublished())
                .course(course)
                .build();

        lesson = lessonRepository.save(lesson);
        return mapToLessonResponse(lesson);
    }

    public LessonResponse getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return mapToLessonResponse(lesson);
    }

    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId).stream()
                .map(this::mapToLessonResponse)
                .collect(Collectors.toList());
    }

    public List<LessonResponse> getLessonsByCourseForInstructor(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to view lessons for this course");
            }
        }

        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId).stream()
                .map(this::mapToLessonResponse)
                .collect(Collectors.toList());
    }

    public List<LessonResponse> getPublishedLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdAndIsPublished(courseId, true).stream()
                .map(this::mapToLessonResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonResponse updateLesson(Long lessonId, LessonRequest request, Long instructorId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Verify instructor owns the course
        if (!lesson.getCourse().getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to update this lesson");
            }
        }

        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setType(request.getType());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setDuration(request.getDuration());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setIsPublished(request.getIsPublished());

        lesson = lessonRepository.save(lesson);
        return mapToLessonResponse(lesson);
    }

    @Transactional
    public void deleteLesson(Long lessonId, Long instructorId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Verify instructor owns the course
        if (!lesson.getCourse().getInstructor().getId().equals(instructorId)) {
            User user = userRepository.findById(instructorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new BadRequestException("You don't have permission to delete this lesson");
            }
        }

        lessonRepository.delete(lesson);
    }

    private LessonResponse mapToLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .type(lesson.getType())
                .contentUrl(lesson.getContentUrl())
                .duration(lesson.getDuration())
                .orderIndex(lesson.getOrderIndex())
                .isPublished(lesson.getIsPublished())
                .courseId(lesson.getCourse().getId())
                .createdAt(lesson.getCreatedAt())
                .build();
    }
}
