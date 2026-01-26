# EduFlex Database Setup Guide

## Prerequisites

- PostgreSQL 12+ installed on your system
- PostgreSQL server running on port 5432 (default)

## Quick Setup Instructions

### Option 1: Using pgAdmin (GUI)

1. Open pgAdmin and connect to your PostgreSQL server
2. Right-click on "Databases" → "Create" → "Database"
3. Name it `eduflex_db`
4. Click "Save"

### Option 2: Using psql (Command Line)

1. Open Command Prompt or PowerShell
2. Run: `psql -U postgres`
3. Enter your PostgreSQL password
4. Create the database:
   ```sql
   CREATE DATABASE eduflex_db;
   ```
5. Verify it was created:
   ```sql
   \l
   ```
6. Exit psql:
   ```sql
   \q
   ```

### Option 3: Using SQL Script

1. Navigate to the backend directory
2. Run: `psql -U postgres -f database-setup.sql`

## Database Configuration

Your application is configured in `application.yml`:

- **Database URL**: `jdbc:postgresql://localhost:5432/eduflex_db`
- **Username**: `postgres`
- **Password**: `postgres`

### Change Default Credentials (Recommended for Production)

If you want to change the database credentials:

1. Update `src/main/resources/application.yml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/eduflex_db
       username: your_username
       password: your_password
   ```

2. Or create an `application-local.yml` for local development:
   ```yaml
   spring:
     datasource:
       username: your_local_username
       password: your_local_password
   ```

## Database Schema

The following tables will be automatically created by Hibernate when you run the application:

### Tables:

1. **users** - Stores user accounts (students, instructors, admins)
2. **courses** - Course information created by instructors
3. **lessons** - Individual lessons within courses
4. **enrollments** - Student course enrollments
5. **progress** - Student progress tracking for each lesson

### Entity Relationships:

- User → Courses (One instructor can create many courses)
- User → Enrollments (One student can enroll in many courses)
- Course → Lessons (One course has many lessons)
- Course → Enrollments (One course can have many students)
- Enrollment → Progress (One enrollment tracks progress for many lessons)

## Verify Setup

After creating the database, start your Spring Boot application:

```bash
cd backend
./mvnw spring-boot:run
```

Or on Windows:

```cmd
cd backend
mvnw.cmd spring-boot:run
```

The application will:

1. Connect to PostgreSQL
2. Automatically create all tables based on your entity models
3. Start on http://localhost:8080

Check the console output for:

```
Hibernate: create table users (...)
Hibernate: create table courses (...)
...
```

## Troubleshooting

### "Database does not exist" Error

- Make sure you created the `eduflex_db` database
- Verify the database name in application.yml matches

### "Connection refused" Error

- Ensure PostgreSQL service is running
- Check if PostgreSQL is running on port 5432
- Verify your firewall allows connections

### "Authentication failed" Error

- Check username/password in application.yml
- Default PostgreSQL user is `postgres`
- Verify password set during PostgreSQL installation

### "Driver not found" Error

- Maven should automatically download PostgreSQL driver
- Run: `mvnw clean install` to download dependencies

## Switching to MySQL (Optional)

If you prefer MySQL instead:

1. Install MySQL Server
2. Create database: `CREATE DATABASE eduflex_db;`
3. Update `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/eduflex_db
       username: root
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       properties:
         hibernate:
           dialect: org.hibernate.dialect.MySQLDialect
   ```
4. Update `pom.xml` to include MySQL dependency instead of PostgreSQL

## Database Management Tools

- **pgAdmin**: GUI tool for PostgreSQL management
- **DBeaver**: Universal database tool (works with PostgreSQL, MySQL, etc.)
- **DataGrip**: JetBrains database IDE
- **psql**: Command-line tool included with PostgreSQL

## Next Steps

1. ✅ Install PostgreSQL
2. ✅ Create `eduflex_db` database
3. ✅ Verify application.yml configuration
4. ✅ Run Spring Boot application
5. ✅ Check that tables are created automatically
6. 📝 (Optional) Insert sample data for testing
7. 📝 (Optional) Set up database backups

## Sample Data (Optional)

To test the application, you can manually insert a test admin user or use your API endpoints to register users.
