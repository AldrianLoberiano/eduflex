-- EduFlex Database Creation Script
--
-- HOW TO RUN THIS FILE:
--
-- Option 1: Using psql command line (RECOMMENDED)
--   cd "C:\Program Files\PostgreSQL\18\bin"
--   psql -U postgres -c "DROP DATABASE IF EXISTS eduflex_db;"
--   psql -U postgres -c "CREATE DATABASE eduflex_db;"
--
-- Option 2: Using pgAdmin (if DROP DATABASE error occurs, run commands separately)
--   First run this:
--     DROP DATABASE IF EXISTS eduflex_db;
--   Then run this:
--     CREATE DATABASE eduflex_db WITH OWNER = postgres ENCODING = 'UTF8';
--
-- Option 3: Simple creation (if database doesn't exist yet)
--   Just run the CREATE DATABASE command below

-- NOTE: DROP DATABASE cannot run inside a transaction block
-- If you get that error, run the commands separately or use psql with -c flag

-- Create the new database (comment out if database already exists)
CREATE DATABASE eduflex_db
WITH
    OWNER = postgres ENCODING = 'UTF8' CONNECTION
LIMIT = -1;

-- Optional: If you want to create a dedicated user (uncomment below)
-- DROP USER IF EXISTS eduflex_user;
-- CREATE USER eduflex_user WITH PASSWORD 'your_secure_password';
-- GRANT ALL PRIVILEGES ON DATABASE eduflex_db TO eduflex_user;

-- Note: Spring Boot will automatically create all tables when you start the application
-- The JPA configuration (ddl-auto: update) will handle schema creation