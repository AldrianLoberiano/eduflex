package com.eduflex.service;

import com.eduflex.dto.response.EnrollmentResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.Course;
import com.eduflex.model.entity.Enrollment;
import com.eduflex.model.entity.User;
import com.eduflex.repository.CourseRepository;
import com.eduflex.repository.EnrollmentRepository;
import com.eduflex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrollmentResponse enrollInCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new BadRequestException("Already enrolled in this course");
        }

        // Check if course is published
        if (!course.getIsPublished()) {
            throw new BadRequestException("Cannot enroll in unpublished course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .progressPercentage(0)
                .build();

        enrollment = enrollmentRepository.save(enrollment);
        return mapToEnrollmentResponse(enrollment);
    }

    public EnrollmentResponse getEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return mapToEnrollmentResponse(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unenroll(Long enrollmentId, Long studentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new BadRequestException("You don't have permission to unenroll from this course");
        }

        enrollmentRepository.delete(enrollment);
    }

    @Transactional
    public EnrollmentResponse updateProgress(Long enrollmentId, Integer progressPercentage) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setProgressPercentage(progressPercentage);

        // Mark as completed if progress is 100%
        if (progressPercentage >= 100 && enrollment.getCompletedAt() == null) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        enrollment = enrollmentRepository.save(enrollment);
        return mapToEnrollmentResponse(enrollment);
    }

    private EnrollmentResponse mapToEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFullName())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .courseCategory(
                        enrollment.getCourse().getCategory() != null
                                ? enrollment.getCourse().getCategory()
                                : "UNCATEGORIZED")
                .progressPercentage(enrollment.getProgressPercentage())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .isCompleted(enrollment.isCompleted())
                .build();
    }
}
