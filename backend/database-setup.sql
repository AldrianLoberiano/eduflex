-- Database Setup Script for EduFlex
-- Run this in PostgreSQL after installation

-- Create the database
CREATE DATABASE eduflex_db;

-- Connect to the database
\c eduflex_db;

-- Verify database is empty (Spring Boot will create tables automatically)
SELECT 'Database eduflex_db created successfully!' as status;

-- Optional: Create a custom user (if you don't want to use default postgres user)
-- CREATE USER eduflex_user WITH PASSWORD 'your_secure_password';
-- GRANT ALL PRIVILEGES ON DATABASE eduflex_db TO eduflex_user;