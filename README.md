# EduFlex - Educational Learning Platform

A comprehensive full-stack educational platform built with Vue.js 3 and Spring Boot, enabling course management, quiz creation, and student progress tracking.

## Features

### Authentication & Authorization

- JWT-based secure authentication
- Role-based access control (Student, Instructor, Admin)
- Auto-redirect based on user roles
- Protected routes with navigation guards
- Session management with token refresh

### Course Management

- Browse and search published courses
- Detailed course information with lessons
- Course enrollment system for students
- Create, edit, and delete courses (Instructors)
- Publish/unpublish course visibility
- Course categorization and organization

### Quiz System

- Interactive quiz creation and management
- Multiple-choice question support
- Quiz assignment to courses
- Real-time quiz taking interface
- Automatic grading and scoring
- Quiz results and performance tracking

### Student Features

- Personal learning dashboard
- Course enrollment and progress tracking
- Access to enrolled course materials
- Take quizzes and view results
- Track completion percentage
- Learning history and achievements

### Instructor Features

- Instructor dashboard with analytics
- Create and manage multiple courses
- Add lessons and course content
- Create and assign quizzes
- View enrolled students
- Monitor student progress
- Course publishing controls

### Admin Features

- System-wide user management (13 comprehensive features)
- User approvals and verification
- Course management and categories
- Module and content management
- Enrollment oversight
- Assessment and grading system
- Attendance tracking
- Communication tools and announcements
- Advanced reports and analytics
- Payment and subscription management
- System settings and configuration
- Content moderation
- Platform administration tools

### UI/UX

- Fully responsive design
- Modern and intuitive interface
- Real-time updates
- Modal-based authentication
- Loading states and error handling
- Toast notifications

## Tech Stack

### Frontend

- **Vue 3** - Progressive JavaScript framework with Composition API
- **Vite** - Next generation frontend build tool
- **Vue Router** - Official routing library
- **Pinia** - State management solution
- **Axios** - Promise-based HTTP client

### Backend

- **Spring Boot 3** - Java application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access layer
- **PostgreSQL** - Primary database
- **JWT** - JSON Web Tokens for authentication
- **Hibernate** - ORM framework
- **Maven** - Dependency management

## Getting Started

### Prerequisites

- **Node.js 16+** - [Download here](https://nodejs.org/)
- **PostgreSQL 12+** - [Download here](https://www.postgresql.org/download/)
- **Java 17+** - [Download here](https://adoptium.net/)
- **Maven 3.6+** - (included with backend via Maven Wrapper)

---

## Database Setup (PostgreSQL)

### Option 1: Using pgAdmin (GUI)

1. Open **pgAdmin** and connect to your PostgreSQL server
2. Right-click on **"Databases"** → **"Create"** → **"Database"**
3. Name it: `eduflex_db`
4. Click **"Save"**

### Option 2: Using psql (Command Line)

1. Open Command Prompt or PowerShell
2. Connect to PostgreSQL:
   ```bash
   psql -U postgres
   ```
3. Enter your PostgreSQL password when prompted
4. Create the database:
   ```sql
   CREATE DATABASE eduflex_db;
   ```
5. Verify creation:
   ```sql
   \l
   ```
6. Exit psql:
   ```sql
   \q
   ```

### Option 3: Using SQL Script

Navigate to the backend directory and run:

```bash
psql -U postgres -f database-setup.sql
```

### Database Configuration

The backend is configured to connect to PostgreSQL with these default settings in `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eduflex_db
    username: postgres
    password: admin123
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update # Auto-creates tables
    show-sql: true
```

**⚠️ Important:** Update the password in `application.yml` to match your PostgreSQL installation.

### Database Schema

Tables are automatically created by Hibernate on first run:

| Table             | Description                                   |
| ----------------- | --------------------------------------------- |
| **users**         | User accounts (students, instructors, admins) |
| **courses**       | Course information created by instructors     |
| **lessons**       | Individual lessons within courses             |
| **enrollments**   | Student course enrollments                    |
| **progress**      | Student progress tracking for each lesson     |
| **quizzes**       | Quiz information and questions                |
| **quiz_attempts** | Student quiz attempt records                  |
| **quiz_results**  | Quiz scores and results                       |

### Entity Relationships

- **User** → **Courses** (One instructor creates many courses)
- **User** → **Enrollments** (One student enrolls in many courses)
- **Course** → **Lessons** (One course has many lessons)
- **Course** → **Quizzes** (One course has many quizzes)
- **Course** → **Enrollments** (One course has many enrolled students)
- **Enrollment** → **Progress** (One enrollment tracks progress for many lessons)
- **Quiz** → **Quiz Attempts** (Students can attempt quizzes)

---

## Installation & Running

### Backend Setup

1. **Navigate to backend directory:**

   ```bash
   cd backend
   ```

2. **Ensure PostgreSQL database is created** (see Database Setup above)

3. **Update database credentials** in `src/main/resources/application.yml` if needed

4. **Build and run the application:**

   On Windows:

   ```bash
   mvnw.cmd spring-boot:run
   ```

   On Mac/Linux:

   ```bash
   ./mvnw spring-boot:run
   ```

   Or build and run the JAR:

   ```bash
   mvnw.cmd clean package
   java -jar target/eduflex-backend-1.0.0.jar
   ```

5. **Verify backend is running:**
   - Open browser to http://localhost:8080/api/health
   - You should see: `{"status":"UP","message":"EduFlex API is running"}`

### Frontend Setup

1. **Navigate to frontend directory:**

   ```bash
   cd frontend
   ```

2. **Install dependencies:**

   ```bash
   npm install
   ```

3. **Start development server:**

   ```bash
   npm run dev
   ```

4. **Access the application:**
   - Open browser to http://localhost:5173 (or the URL shown in terminal)

### Default Test Accounts

The application initializes with default accounts (see `DataInitializer.java`):

| Role           | Email                  | Password      |
| -------------- | ---------------------- | ------------- |
| **Admin**      | admin@eduflex.com      | admin123      |
| **Instructor** | instructor@eduflex.com | instructor123 |
| **Student**    | student@eduflex.com    | student123    |

---

## Project Structure

### Frontend Structure

```
frontend/
├── src/
│   ├── assets/              # Static assets and global styles
│   │   └── styles.css       # Global CSS styles
│   ├── components/          # Reusable Vue components
│   │   ├── courses/         # Course-related components
│   │   │   └── CourseCard.vue
│   │   ├── layout/          # Layout components
│   │   │   ├── Navbar.vue   # Navigation bar
│   │   │   ├── Footer.vue   # Footer component
│   │   │   └── Sidebar.vue  # Sidebar navigation
│   │   ├── LoginModal.vue   # Login modal component
│   │   ├── RegisterModal.vue # Registration modal
│   │   ├── QuizForm.vue     # Quiz creation form
│   │   ├── QuizList.vue     # Quiz listing component
│   │   └── QuizTaker.vue    # Interactive quiz interface
│   ├── router/              # Vue Router configuration
│   │   └── index.js         # Route definitions
│   ├── services/            # API service layer
│   │   ├── api.js           # Axios instance & interceptors
│   │   └── index.js         # Service exports
│   ├── stores/              # Pinia state management
│   │   └── auth.js          # Authentication store
│   ├── views/               # Page components
│   │   ├── Courses.vue      # Course browsing page
│   │   ├── CourseQuizzes.vue # Course quiz page
│   │   ├── admin/           # Admin pages
│   │   │   ├── Dashboard.vue
│   │   │   ├── Profile.vue
│   │   │   └── CourseDetail.vue
│   │   ├── instructor/      # Instructor pages
│   │   │   ├── Dashboard.vue
│   │   │   ├── MyCourses.vue
│   │   │   ├── CreateCourse.vue
│   │   │   ├── EditCourse.vue
│   │   │   └── CourseStudents.vue
│   │   └── student/         # Student pages
│   │       └── MyLearning.vue
│   ├── App.vue              # Root component
│   └── main.js              # Application entry point
├── index.html               # HTML template
├── package.json             # Dependencies
├── vite.config.js           # Vite configuration
└── README.md

### Backend Structure
```

backend/
├── src/main/
│ ├── java/com/eduflex/
│ │ ├── EduFlexApplication.java # Main Spring Boot application
│ │ ├── config/ # Configuration classes
│ │ │ ├── CorsConfig.java # CORS configuration
│ │ │ ├── SecurityConfig.java # Security & JWT config
│ │ │ └── DataInitializer.java # Initial data seeding
│ │ ├── controller/ # REST Controllers
│ │ │ ├── AuthController.java # Authentication endpoints
│ │ │ ├── CourseController.java # Course management
│ │ │ ├── StudentController.java # Student operations
│ │ │ ├── InstructorController.java # Instructor operations
│ │ │ ├── QuizController.java # Quiz management
│ │ │ ├── AdminController.java # Admin operations
│ │ │ └── HealthController.java # Health check
│ │ ├── dto/ # Data Transfer Objects
│ │ │ ├── request/ # Request DTOs
│ │ │ └── response/ # Response DTOs
│ │ ├── exception/ # Exception handling
│ │ │ ├── GlobalExceptionHandler.java
│ │ │ ├── ResourceNotFoundException.java
│ │ │ └── BadRequestException.java
│ │ ├── model/ # Domain models
│ │ │ ├── entity/ # JPA entities
│ │ │ └── enums/ # Enumerations
│ │ ├── repository/ # Data access layer
│ │ │ └── CourseRepository.java # JPA repositories
│ │ ├── security/ # Security components
│ │ │ └── JwtTokenProvider.java # JWT utilities
│ │ └── service/ # Business logic layer
│ └── resources/
│ └── application.yml # Application configuration
├── DATABASE_SETUP.md # Database setup guide
├── database-setup.sql # SQL initialization script
├── pom.xml # Maven dependencies
├── mvnw / mvnw.cmd # Maven wrapper scripts
└── target/ # Build output

````

---

## User Roles & Permissions

### Student Role
**Capabilities:**
- Browse and search all published courses
- View detailed course information and syllabus
- Enroll in courses
- Access enrolled course materials and lessons
- Take quizzes and assessments
- View quiz results and scores
- Track personal learning progress
- Monitor course completion percentage
- View learning history and achievements
- Update profile information

**Restrictions:**
- Cannot create or modify courses
- Cannot access instructor or admin features
- Can only view their own enrolled courses

### Instructor Role
**Capabilities:**
- Access instructor dashboard with analytics
- Create new courses with descriptions
- Edit and delete own courses
- Add, edit, and remove lessons
- Create and manage quizzes
- Assign quizzes to courses
- Publish/unpublish courses
- View list of enrolled students
- Monitor student progress and performance
- View quiz attempt statistics
- Manage course content and structure

**Restrictions:**
- Cannot modify other instructors' courses
- Cannot access admin-level system settings
- Cannot manage users (except viewing enrolled students)

### Admin Role
**Capabilities:**
- Access to all system features
- Manage all users (create, edit, delete, activate/deactivate)
- View and moderate all courses
- Access system-wide analytics and reports
- Manage platform settings and configurations
- View all quizzes and results
- Override permissions when necessary
- Monitor system health and performance
- Access admin dashboard with full statistics

**Restrictions:**
- Should follow platform policies and guidelines
- Admin actions are logged for accountability

---

## API Integration

### Backend API Endpoints

**Base URL:** `http://localhost:8080/api`

#### Authentication Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login (returns JWT token)
- `POST /api/auth/refresh` - Refresh JWT token
- `GET /api/auth/me` - Get current user info

#### Course Endpoints
- `GET /api/courses` - Get all published courses
- `GET /api/courses/{id}` - Get course by ID
- `POST /api/instructor/courses` - Create new course (Instructor)
- `PUT /api/instructor/courses/{id}` - Update course (Instructor)
- `DELETE /api/instructor/courses/{id}` - Delete course (Instructor)

#### Student Endpoints
- `POST /api/student/enroll/{courseId}` - Enroll in a course
- `GET /api/student/my-courses` - Get enrolled courses
- `GET /api/student/progress/{courseId}` - Get course progress
- `POST /api/student/progress` - Update lesson progress

#### Quiz Endpoints
- `GET /api/courses/{courseId}/quizzes` - Get quizzes for a course
- `POST /api/instructor/quizzes` - Create quiz (Instructor)
- `PUT /api/instructor/quizzes/{id}` - Update quiz (Instructor)
- `DELETE /api/instructor/quizzes/{id}` - Delete quiz (Instructor)
- `POST /api/student/quizzes/{id}/attempt` - Submit quiz attempt
- `GET /api/student/quizzes/results` - Get quiz results

#### Admin Endpoints
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}` - Update user
- `DELETE /api/admin/users/{id}` - Delete user
- `GET /api/admin/analytics` - Get platform analytics

### Authentication Flow

1. **Login/Register:** User submits credentials → Backend returns JWT token
2. **Token Storage:** Frontend stores token in localStorage
3. **API Requests:** Axios interceptor automatically adds token to headers:
   ```javascript
   Authorization: Bearer <jwt-token>
````

4. **Token Validation:** Backend validates token on each protected request
5. **Token Refresh:** Automatically refreshes token before expiration

### Error Handling

The API uses standard HTTP status codes:

- `200` - Success
- `201` - Created
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (invalid/expired token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `500` - Internal Server Error

---

## Available Scripts

### Frontend Scripts

```bash
npm run dev        # Start development server (http://localhost:5173)
npm run build      # Build for production (output to dist/)
npm run preview    # Preview production build locally
```

### Backend Scripts

```bash
# Windows
mvnw.cmd clean package          # Build project
mvnw.cmd spring-boot:run        # Run application
mvnw.cmd test                   # Run tests

# Mac/Linux
./mvnw clean package            # Build project
./mvnw spring-boot:run          # Run application
./mvnw test                     # Run tests
```

---

## Features Overview

### Authentication System

- **JWT-based authentication** with secure token management
- **Role-based access control** (RBAC) for different user types
- **Auto-redirect** based on user role after login
- **Protected routes** with navigation guards
- **Session persistence** with localStorage
- **Token refresh** mechanism for extended sessions
- **Password encryption** using BCrypt

### Course Management System

- **Course catalog** with search and filtering
- **Detailed course pages** with descriptions and syllabi
- **Enrollment system** for students
- **Course creation wizard** for instructors
- **Lesson management** with ordering
- **Course publishing controls** (draft/published states)
- **Student roster** viewing for instructors
- **Course analytics** and enrollment statistics

### Quiz & Assessment System

- **Interactive quiz builder** with drag-and-drop interface
- **Multiple-choice questions** with single/multiple answers
- **Quiz assignment** to specific courses
- **Real-time quiz taking** with timer support
- **Automatic grading** and instant feedback
- **Score calculation** and performance metrics
- **Quiz attempt history** and analytics
- **Results dashboard** for students and instructors

### Progress Tracking

- **Course completion percentage** calculation
- **Lesson-by-lesson progress** tracking
- **Visual progress indicators** (progress bars, charts)
- **Learning history** and timeline
- **Achievement milestones** and badges (planned)
- **Performance analytics** for students
- **Instructor dashboard** with student progress overview

### User Interface

- **Responsive design** - Works on desktop, tablet, and mobile
- **Modern UI** with clean and intuitive layouts
- **Modal dialogs** for login, registration, and forms
- **Loading states** and skeleton screens
- **Error handling** with user-friendly messages
- **Toast notifications** for feedback
- **Dark mode support** (planned)
- **Accessibility features** following WCAG guidelines

---

## Configuration

### Frontend Environment Variables

Create a `.env` file in the frontend directory:

```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=EduFlex
```

### Backend Configuration

Edit `backend/src/main/resources/application.yml`:

```yaml
# Server Configuration
server:
  port: 8080

# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eduflex_db
    username: postgres
    password: your_password_here

# JWT Configuration
jwt:
  secret: your-secret-key-here
  expiration: 86400000 # 24 hours
  refresh-expiration: 604800000 # 7 days

# CORS Configuration (adjust for production)
cors:
  allowed-origins: http://localhost:5173,http://localhost:3000
```

### Production Configuration

For production deployment, create `application-prod.yml`:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate # Never use 'update' in production
    show-sql: false

jwt:
  secret: ${JWT_SECRET}

cors:
  allowed-origins: https://your-domain.com
```

---

## Testing

### Backend Testing

```bash
# Run all tests
mvnw.cmd test

# Run specific test class
mvnw.cmd test -Dtest=CourseControllerTest

# Run with coverage report
mvnw.cmd test jacoco:report
```

### Frontend Testing (Planned)

```bash
npm run test          # Run unit tests
npm run test:e2e      # Run end-to-end tests
npm run test:coverage # Generate coverage report
```

---

## Deployment

### Frontend Deployment

#### Build for Production

```bash
cd frontend
npm run build
```

The production-ready files will be in the `dist/` directory.

#### Deploy to Netlify/Vercel

1. Connect your GitHub repository
2. Set build command: `npm run build`
3. Set publish directory: `dist`
4. Add environment variables (if needed)

#### Deploy to Apache/Nginx

Copy the contents of `dist/` to your web server directory.

**Nginx configuration example:**

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/eduflex/dist;

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### Backend Deployment

#### Build JAR File

```bash
cd backend
mvnw.cmd clean package
```

The JAR file will be at `target/eduflex-backend-1.0.0.jar`

#### Run on Server

```bash
java -jar eduflex-backend-1.0.0.jar
```

#### Deploy with Docker

Create `Dockerfile` in backend directory:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/eduflex-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:

```bash
docker build -t eduflex-backend .
docker run -p 8080:8080 eduflex-backend
```

#### Deploy to Cloud Platforms

- **AWS:** Elastic Beanstalk, ECS, or EC2
- **Azure:** App Service or Container Instances
- **Google Cloud:** App Engine or Cloud Run
- **Heroku:** Connect GitHub and deploy

---

## Troubleshooting

### Database Connection Issues

```
Error: Connection refused to localhost:5432
```

**Solution:**

- Ensure PostgreSQL is running: `pg_ctl status`
- Check credentials in `application.yml`
- Verify database exists: `psql -U postgres -l`

### Port Already in Use

```
Error: Port 8080 is already in use
```

**Solution:**

- Find process using port: `netstat -ano | findstr :8080`
- Kill process: `taskkill /PID <process-id> /F`
- Or change port in `application.yml`

### CORS Issues

```
Error: CORS policy: No 'Access-Control-Allow-Origin' header
```

**Solution:**

- Verify `CorsConfig.java` includes your frontend URL
- Check allowed origins in backend configuration
- Clear browser cache

### JWT Token Expired

```
Error: 401 Unauthorized
```

**Solution:**

- Token expires after 24 hours by default
- Log out and log back in
- Implement token refresh mechanism

### Build Failures

```
Error: Could not resolve dependencies
```

**Solution:**

- Run `mvnw.cmd clean install`
- Check internet connection
- Clear Maven cache: Delete `~/.m2/repository`

---

## Browser Support

| Browser | Version | Status          |
| ------- | ------- | --------------- |
| Chrome  | Latest  | Fully Supported |
| Firefox | Latest  | Fully Supported |
| Safari  | Latest  | Fully Supported |
| Edge    | Latest  | Fully Supported |
| Opera   | Latest  | Fully Supported |
| IE 11   | -       | Not Supported   |

---

## Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**

   ```bash
   git clone https://github.com/yourusername/eduflex.git
   ```

2. **Create a feature branch**

   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make your changes**
   - Follow code style guidelines
   - Write meaningful commit messages
   - Add tests for new features

4. **Commit your changes**

   ```bash
   git commit -m "Add: Amazing new feature"
   ```

5. **Push to your branch**

   ```bash
   git push origin feature/amazing-feature
   ```

6. **Open a Pull Request**
   - Describe your changes
   - Reference any related issues
   - Wait for code review

### Code Style Guidelines

- **Java:** Follow Google Java Style Guide
- **JavaScript:** Use ESLint configuration
- **Vue:** Follow Vue.js Style Guide
- **Commit Messages:** Use conventional commits format

---

##  Roadmap

### Phase 1 (Current)

-  User authentication and authorization
-  Course management system
-  Quiz creation and taking
-  Progress tracking
-  Role-based access control

### Phase 2 (In Progress)

-  Video lesson support
-  File upload for course materials
-  Discussion forums
-  Real-time notifications
-  Certificate generation

### Phase 3 (Planned)

-  Live streaming classes
-  Payment integration
-  Mobile app (React Native)
-  Advanced analytics and reporting
-  Gamification features
-  Multi-language support
-  AI-powered recommendations

---

## Acknowledgments

- **Spring Boot** - Backend framework
- **Vue.js** - Frontend framework
- **PostgreSQL** - Database system
- **JWT.io** - Token implementation
- **Vite** - Build tool
- All open-source contributors

---

## Copyright Notice

Copyright © 2026 Aldrian Loberiano.  
This project is intended strictly for personal use.  
Any unauthorized commercial use, distribution, or copying of the code or materials in this repository is prohibited and may constitute copyright infringement.

