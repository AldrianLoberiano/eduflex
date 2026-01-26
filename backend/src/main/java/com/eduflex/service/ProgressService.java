package com.eduflex.service;

import com.eduflex.dto.response.ProgressResponse;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.Enrollment;
import com.eduflex.model.entity.Lesson;
import com.eduflex.model.entity.Progress;
import com.eduflex.repository.EnrollmentRepository;
import com.eduflex.repository.LessonRepository;
import com.eduflex.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentService enrollmentService;

    @Transactional
    public ProgressResponse trackProgress(Long enrollmentId, Long lessonId, Integer watchedDuration) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Get or create progress
        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollmentId, lessonId)
                .orElse(Progress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .completed(false)
                        .build());

        progress.setWatchedDuration(watchedDuration);
        progress.setLastAccessedAt(LocalDateTime.now());

        // Mark as completed if watched duration meets threshold
        if (lesson.getDuration() != null && watchedDuration != null) {
            if (watchedDuration >= lesson.getDuration() * 0.9) { // 90% completion threshold
                if (!progress.getCompleted()) {
                    progress.setCompleted(true);
                    progress.setCompletedAt(LocalDateTime.now());
                }
            }
        }

        progress = progressRepository.save(progress);

        // Update enrollment progress percentage
        updateEnrollmentProgress(enrollmentId);

        return mapToProgressResponse(progress);
    }

    @Transactional
    public ProgressResponse markLessonComplete(Long enrollmentId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollmentId, lessonId)
                .orElse(Progress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .build());

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        progress.setLastAccessedAt(LocalDateTime.now());

        progress = progressRepository.save(progress);

        // Update enrollment progress percentage
        updateEnrollmentProgress(enrollmentId);

        return mapToProgressResponse(progress);
    }

    public List<ProgressResponse> getEnrollmentProgress(Long enrollmentId) {
        return progressRepository.findByEnrollmentId(enrollmentId).stream()
                .map(this::mapToProgressResponse)
                .collect(Collectors.toList());
    }

    private void updateEnrollmentProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        List<Lesson> courseLessons = lessonRepository.findByCourseId(enrollment.getCourse().getId());
        long totalLessons = courseLessons.size();

        if (totalLessons == 0) {
            return;
        }

        long completedLessons = progressRepository.countByEnrollmentIdAndCompleted(enrollmentId, true);
        int progressPercentage = (int) ((completedLessons * 100) / totalLessons);

        enrollmentService.updateProgress(enrollmentId, progressPercentage);
    }

    private ProgressResponse mapToProgressResponse(Progress progress) {
        return ProgressResponse.builder()
                .id(progress.getId())
                .lessonId(progress.getLesson().getId())
                .lessonTitle(progress.getLesson().getTitle())
                .completed(progress.getCompleted())
                .watchedDuration(progress.getWatchedDuration())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .build();
    }
}
