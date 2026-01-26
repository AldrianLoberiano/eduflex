package com.eduflex.config;

import com.eduflex.model.entity.Course;
import com.eduflex.model.entity.Lesson;
import com.eduflex.model.entity.User;
import com.eduflex.model.enums.LessonType;
import com.eduflex.model.enums.UserRole;
import com.eduflex.model.enums.UserStatus;
import com.eduflex.repository.CourseRepository;
import com.eduflex.repository.LessonRepository;
import com.eduflex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Create default admin user if not exists
        if (!userRepository.existsByEmail("admin@eduflex.com")) {
            User admin = User.builder()
                    .email("admin@eduflex.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("EduFlex")
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();

            userRepository.save(admin);
            log.info("=================================================");
            log.info("Default Admin Account Created:");
            log.info("Email: admin@eduflex.com");
            log.info("Password: admin123");
            log.info("=================================================");
        }

        // Create default instructor for testing
        if (!userRepository.existsByEmail("instructor@eduflex.com")) {
            User instructor = User.builder()
                    .email("instructor@eduflex.com")
                    .password(passwordEncoder.encode("instructor123"))
                    .firstName("John")
                    .lastName("Instructor")
                    .role(UserRole.INSTRUCTOR)
                    .status(UserStatus.ACTIVE)
                    .build();

            userRepository.save(instructor);
            log.info("Default Instructor Account Created:");
            log.info("Email: instructor@eduflex.com");
            log.info("Password: instructor123");
        }

        // Create default student for testing
        if (!userRepository.existsByEmail("student@eduflex.com")) {
            User student = User.builder()
                    .email("student@eduflex.com")
                    .password(passwordEncoder.encode("student123"))
                    .firstName("Jane")
                    .lastName("Student")
                    .role(UserRole.STUDENT)
                    .status(UserStatus.ACTIVE)
                    .build();

            userRepository.save(student);
            log.info("Default Student Account Created:");
            log.info("Email: student@eduflex.com");
            log.info("Password: student123");
        }

        // Create sample courses and lessons
        User instructor = userRepository.findByEmail("instructor@eduflex.com").orElse(null);
        if (instructor != null && courseRepository.count() == 0) {
            // Create sample course
            Course course = Course.builder()
                    .title("Introduction to Java Programming")
                    .description("Learn the fundamentals of Java programming from scratch")
                    .category("Programming")
                    .level("Beginner")
                    .duration(480)
                    .isPublished(true)
                    .instructor(instructor)
                    .build();
            course = courseRepository.save(course);

            // Create sample lessons
            Lesson lesson1 = Lesson.builder()
                    .title("Getting Started with Java")
                    .description("Introduction to Java development environment")
                    .type(LessonType.VIDEO)
                    .contentUrl("https://example.com/video1")
                    .duration(30)
                    .orderIndex(1)
                    .isPublished(true)
                    .course(course)
                    .build();
            lessonRepository.save(lesson1);

            Lesson lesson2 = Lesson.builder()
                    .title("Variables and Data Types")
                    .description("Understanding Java variables and data types")
                    .type(LessonType.VIDEO)
                    .contentUrl("https://example.com/video2")
                    .duration(45)
                    .orderIndex(2)
                    .isPublished(true)
                    .course(course)
                    .build();
            lessonRepository.save(lesson2);

            log.info("Sample course and lessons created!");
        }
    }
}
