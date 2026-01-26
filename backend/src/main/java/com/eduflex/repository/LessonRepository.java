package com.eduflex.repository;

import com.eduflex.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);

    List<Lesson> findByCourseIdOrderByOrderIndexAsc(Long courseId);

    List<Lesson> findByCourseIdAndIsPublished(Long courseId, Boolean isPublished);
}
