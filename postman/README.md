# EduFlex API Postman Collection

This folder contains Postman collection and environment files for testing the EduFlex Learning Management System API.

## Files

- **EduFlex-Collection.postman_collection.json** - Complete API collection with all endpoints
- **EduFlex-Local.postman_environment.json** - Local development environment (http://localhost:8080)
- **EduFlex-Production.postman_environment.json** - Production environment template

## How to Import into Postman

### 1. Import Collection

1. Open Postman
2. Click **Import** button (top left)
3. Select **EduFlex-Collection.postman_collection.json**
4. Click **Import**

### 2. Import Environments

1. Click **Import** button again
2. Select **EduFlex-Local.postman_environment.json**
3. Click **Import**
4. Repeat for **EduFlex-Production.postman_environment.json** if needed

### 3. Select Environment

1. Click the environment dropdown (top right)
2. Select **EduFlex Local Environment**

## API Collection Structure

The collection includes the following folders:

### 1. Authentication

- Register Student
- Register Instructor
- Register Admin
- Login Student
- Login Instructor
- Login Admin

**Note:** Login requests automatically save the JWT token to environment variables for subsequent requests.

### 2. Courses (Public & Protected)

- Get All Courses
- Get Published Courses
- Get Course by ID
- Get Courses by Category
- Create Course (Instructor/Admin)
- Update Course (Instructor/Admin)
- Delete Course (Instructor/Admin)

### 3. Instructor

- Get My Courses
- Create Course
- Create Lesson
- Get Course Lessons
- Update Lesson
- Get Course Students

### 4. Student

- Enroll in Course
- Get My Enrollments
- Unenroll from Course
- Get Course Lessons
- Get Enrollment Progress
- Track Lesson Progress
- Complete Lesson

### 5. Quizzes

- Create Quiz (Instructor)
- Update Quiz (Instructor)
- Delete Quiz (Instructor)
- Get Quizzes by Course
- Get Quiz by ID
- Submit Quiz (Student)
- Get My Quiz Attempts (Student)
- Get Quiz Attempt Details

### 6. Admin

- Get All Users
- Get Users by Role
- Get Pending Instructors
- Approve Instructor
- Update User Status
- Delete User

## Environment Variables

### Base Configuration

- `base_url` - API base URL (default: http://localhost:8080)

### Student Credentials

- `student_email` - Student email
- `student_password` - Student password
- `student_token` - Auto-populated JWT token after login
- `student_id` - Auto-populated user ID after login

### Instructor Credentials

- `instructor_email` - Instructor email
- `instructor_password` - Instructor password
- `instructor_token` - Auto-populated JWT token after login
- `instructor_id` - Auto-populated user ID after login

### Admin Credentials

- `admin_email` - Admin email
- `admin_password` - Admin password
- `admin_token` - Auto-populated JWT token after login
- `admin_id` - Auto-populated user ID after login

### Dynamic IDs (Auto-populated)

- `course_id` - Created course ID
- `lesson_id` - Created lesson ID
- `enrollment_id` - Created enrollment ID
- `quiz_id` - Created quiz ID
- `quiz_attempt_id` - Quiz attempt ID

## Testing Workflow

### Quick Start Flow

1. **Register Users**
   - Run "Register Student" to create a student account
   - Run "Register Instructor" to create an instructor account
   - Run "Register Admin" to create an admin account

2. **Login**
   - Run "Login Instructor" (token will be saved automatically)

3. **Create Course**
   - Run "Instructor > Create Course" (course_id will be saved automatically)

4. **Create Lesson**
   - Run "Instructor > Create Lesson" (lesson_id will be saved automatically)

5. **Create Quiz**
   - Run "Quizzes > Create Quiz (Instructor)"

6. **Switch to Student**
   - Run "Login Student"

7. **Enroll in Course**
   - Run "Student > Enroll in Course" (enrollment_id will be saved automatically)

8. **Track Progress**
   - Run "Student > Get Course Lessons"
   - Run "Student > Track Lesson Progress"
   - Run "Student > Complete Lesson"

9. **Take Quiz**
   - Run "Quizzes > Get Quizzes by Course"
   - Run "Quizzes > Submit Quiz (Student)"
   - Run "Quizzes > Get My Quiz Attempts (Student)"

### Admin Flow

1. **Login as Admin**
   - Run "Login Admin"

2. **Manage Users**
   - Run "Admin > Get All Users"
   - Run "Admin > Get Pending Instructors"
   - Run "Admin > Approve Instructor"

## Request Examples

### Authentication Request

```json
{
  "email": "student@example.com",
  "password": "password123"
}
```

### Create Course Request

```json
{
  "title": "Introduction to Java Programming",
  "description": "Learn Java from scratch",
  "thumbnailUrl": "https://example.com/thumbnail.jpg",
  "category": "Programming",
  "level": "Beginner",
  "duration": 40,
  "isPublished": true
}
```

### Create Quiz Request

```json
{
  "title": "Java Basics Quiz",
  "description": "Test your knowledge",
  "courseId": 1,
  "timeLimit": 30,
  "passingScore": 70,
  "isPublished": true,
  "questions": [
    {
      "question": "What is Java?",
      "type": "MULTIPLE_CHOICE",
      "points": 10,
      "orderIndex": 1,
      "options": [
        {
          "optionText": "A programming language",
          "isCorrect": true,
          "orderIndex": 1
        }
      ]
    }
  ]
}
```

## Response Format

All API responses follow this structure:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    /* response data */
  },
  "timestamp": "2026-02-05T10:43:22.986+08:00"
}
```

## Authentication

Most endpoints require JWT authentication. The token is automatically added to requests using Bearer authentication:

```
Authorization: Bearer {{token_variable}}
```

Tokens are automatically saved when you login and used in subsequent requests.

## Enum Values

### UserRole

- `STUDENT`
- `INSTRUCTOR`
- `ADMIN`

### UserStatus

- `ACTIVE`
- `SUSPENDED`
- `INACTIVE`

### LessonType

- `VIDEO`
- `ARTICLE`
- `QUIZ`
- `ASSIGNMENT`

### QuestionType

- `MULTIPLE_CHOICE`
- `TRUE_FALSE`
- `SHORT_ANSWER`

## Troubleshooting

### 401 Unauthorized Error

- Make sure you've logged in first
- Check that the token is saved in environment variables
- Token might be expired (default: 24 hours)

### 403 Forbidden Error

- You don't have permission for this endpoint
- Check that you're using the correct role token

### 404 Not Found Error

- Check that the ID in the URL exists
- Verify environment variables are populated correctly

### Database Connection Error

- Make sure PostgreSQL is running
- Check database credentials in application.yml
- Run the database setup scripts

## Monitor Setup

You can create a Postman Monitor to:

- Test API health periodically
- Monitor response times
- Track API availability
- Get alerts on failures

To create a monitor:

1. Go to **Monitors** in Postman
2. Click **Create Monitor**
3. Select **EduFlex API Collection**
4. Choose **EduFlex Local Environment**
5. Set schedule and notifications

## Tips

- Run requests in sequence for proper data flow
- Check the Tests tab for auto-saved variables
- Use the Console to debug requests
- Export environment to save your tokens
- Create multiple environments for different stages

## Support

For issues or questions:

- Check the backend logs for detailed error messages
- Review the API documentation
- Verify database is running and accessible
- Check that all required fields are provided

---

**Version:** 1.0.0  
**Last Updated:** February 5, 2026  
**Backend URL:** http://localhost:8080  
**Frontend URL:** http://localhost:3000
