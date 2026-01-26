package com.eduflex.repository;

import com.eduflex.model.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentIdAndQuizId(Long studentId, Long quizId);

    List<QuizAttempt> findByStudentIdOrderByStartedAtDesc(Long studentId);

    List<QuizAttempt> findByQuizId(Long quizId);

    Optional<QuizAttempt> findTopByStudentIdAndQuizIdOrderByScoreDesc(Long studentId, Long quizId);
}
