package com.eduflex.repository;

import com.eduflex.model.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByEnrollmentId(Long enrollmentId);

    Optional<Progress> findByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);

    long countByEnrollmentIdAndCompleted(Long enrollmentId, Boolean completed);
}
