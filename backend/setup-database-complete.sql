-- ====================================================================
-- EduFlex Complete Database Setup Script
-- ====================================================================
-- This script creates the database, all tables, and sample data
--
-- HOW TO RUN:
-- 1. In pgAdmin: Tools > Query Tool > Open this file > Execute (F5)
-- 2. Or command line:
--    cd "C:\Program Files\PostgreSQL\18\bin"
--    psql -U postgres -f "c:\eduflex\backend\setup-database-complete.sql"
-- ====================================================================

-- Create database (run this first if database doesn't exist)
CREATE DATABASE eduflex_db
WITH
    OWNER = postgres ENCODING = 'UTF8' CONNECTION
LIMIT = -1;

-- Connect to the database (in psql, or switch to eduflex_db in pgAdmin)
-- \c eduflex_db;

-- ====================================================================
-- TABLE CREATION
-- ====================================================================

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (
        role IN (
            'ADMIN',
            'INSTRUCTOR',
            'STUDENT'
        )
    ),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (
        status IN (
            'ACTIVE',
            'INACTIVE',
            'SUSPENDED'
        )
    ),
    profile_image VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    thumbnail_url VARCHAR(500),
    category VARCHAR(100) NOT NULL,
    level VARCHAR(50),
    duration INTEGER,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    instructor_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_instructor FOREIGN KEY (instructor_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Lessons table
CREATE TABLE IF NOT EXISTS lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    type VARCHAR(20) NOT NULL CHECK (
        type IN (
            'VIDEO',
            'TEXT',
            'QUIZ',
            'ASSIGNMENT'
        )
    ),
    duration INTEGER,
    video_url VARCHAR(500),
    order_index INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_percentage INTEGER DEFAULT 0,
    completed_at TIMESTAMP,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT unique_enrollment UNIQUE (student_id, course_id)
);

-- Progress table
CREATE TABLE IF NOT EXISTS progress (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    time_spent INTEGER,
    CONSTRAINT fk_progress_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_progress_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    CONSTRAINT unique_progress UNIQUE (student_id, lesson_id)
);

-- Quizzes table
CREATE TABLE IF NOT EXISTS quizzes (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration INTEGER,
    passing_score INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_quiz_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

-- Quiz Questions table
CREATE TABLE IF NOT EXISTS quiz_questions (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    points INTEGER DEFAULT 1,
    order_index INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE
);

-- Quiz Options table
CREATE TABLE IF NOT EXISTS quiz_options (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES quiz_questions (id) ON DELETE CASCADE
);

-- Quiz Attempts table
CREATE TABLE IF NOT EXISTS quiz_attempts (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    score INTEGER DEFAULT 0,
    max_score INTEGER DEFAULT 0,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP,
    CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE,
    CONSTRAINT fk_attempt_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Quiz Answers table
CREATE TABLE IF NOT EXISTS quiz_answers (
    id BIGSERIAL PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT,
    is_correct BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_answer_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES quiz_questions (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_option FOREIGN KEY (selected_option_id) REFERENCES quiz_options (id) ON DELETE SET NULL
);

-- ====================================================================
-- INDEXES FOR PERFORMANCE
-- ====================================================================

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

CREATE INDEX IF NOT EXISTS idx_users_role ON users (role);

CREATE INDEX IF NOT EXISTS idx_courses_instructor ON courses (instructor_id);

CREATE INDEX IF NOT EXISTS idx_courses_published ON courses (is_published);

CREATE INDEX IF NOT EXISTS idx_lessons_course ON lessons (course_id);

CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments (student_id);

CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments (course_id);

CREATE INDEX IF NOT EXISTS idx_progress_student ON progress (student_id);

CREATE INDEX IF NOT EXISTS idx_progress_lesson ON progress (lesson_id);

CREATE INDEX IF NOT EXISTS idx_quizzes_course ON quizzes (course_id);

CREATE INDEX IF NOT EXISTS idx_quiz_attempts_student ON quiz_attempts (student_id);

-- ====================================================================
-- SAMPLE DATA
-- ====================================================================

-- Insert default users (passwords are BCrypt hashed)
-- Password for all users: admin123, instructor123, student123
INSERT INTO
    users (
        email,
        password,
        first_name,
        last_name,
        role,
        status
    )
VALUES (
        'admin@eduflex.com',
        '$2a$10$YourHashedPasswordHere',
        'Admin',
        'EduFlex',
        'ADMIN',
        'ACTIVE'
    ),
    (
        'instructor@eduflex.com',
        '$2a$10$YourHashedPasswordHere',
        'John',
        'Instructor',
        'INSTRUCTOR',
        'ACTIVE'
    ),
    (
        'student@eduflex.com',
        '$2a$10$YourHashedPasswordHere',
        'Jane',
        'Student',
        'STUDENT',
        'ACTIVE'
    ) ON CONFLICT (email) DO NOTHING;

-- Note: The actual passwords will be created by Spring Boot's DataInitializer
-- which uses BCrypt hashing. The above is just a placeholder.
--
-- After running the Spring Boot application, you can log in with:
-- Admin: admin@eduflex.com / admin123
-- Instructor: instructor@eduflex.com / instructor123
-- Student: student@eduflex.com / student123

-- ====================================================================
-- COMPLETION MESSAGE
-- ====================================================================

SELECT 'Database setup completed successfully!' AS status, 'Tables created: users, courses, lessons, enrollments, progress, quizzes, quiz_questions, quiz_options, quiz_attempts, quiz_answers' AS info, 'Run your Spring Boot application to populate sample data' AS next_step;

-- ====================================================================
-- VERIFICATION QUERIES (Optional - run to check)
-- ====================================================================

-- Check all tables
-- SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

-- Check user count
-- SELECT COUNT(*) as user_count FROM users;

-- View all users
-- SELECT id, email, first_name, last_name, role, status FROM users;