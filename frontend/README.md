# EduFlex Frontend

Vue.js 3 frontend for the EduFlex educational platform.

## Features

- 🔐 User authentication (Login/Register)
- 📚 Course browsing and enrollment
- 👨‍🎓 Student dashboard with progress tracking
- 👨‍🏫 Instructor dashboard for course management
- 👑 Admin panel (coming soon)
- 📱 Responsive design

## Tech Stack

- **Vue 3** - Progressive JavaScript framework
- **Vite** - Next generation frontend tooling
- **Vue Router** - Official router for Vue.js
- **Pinia** - State management
- **Axios** - HTTP client

## Getting Started

### Prerequisites

- Node.js 16+ installed
- Backend server running on http://localhost:8080

### Installation

1. Navigate to the frontend directory:
```bash
cd eduflex/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The application will be available at http://localhost:3000

### Build for Production

```bash
npm run build
```

Preview production build:
```bash
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── assets/          # Static assets and styles
│   ├── components/      # Reusable Vue components
│   │   ├── courses/     # Course-related components
│   │   └── layout/      # Layout components (Navbar, etc.)
│   ├── router/          # Vue Router configuration
│   ├── services/        # API services
│   ├── stores/          # Pinia stores
│   ├── views/           # Page components
│   │   ├── admin/       # Admin pages
│   │   ├── instructor/  # Instructor pages
│   │   └── student/     # Student pages
│   ├── App.vue          # Root component
│   └── main.js          # Application entry point
├── index.html
├── package.json
└── vite.config.js
```

## User Roles

### Student
- Browse and view courses
- Enroll in courses
- Track learning progress
- Access enrolled course content

### Instructor
- Create and manage courses
- Add lessons to courses
- View student enrollments
- Publish/unpublish courses

### Admin
- Manage all users
- Moderate content
- View system analytics
- (Features coming soon)

## API Integration

The frontend communicates with the Spring Boot backend via REST API:
- Base URL: http://localhost:8080/api
- Authentication: JWT tokens stored in localStorage
- Automatic token injection via Axios interceptors

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build

## Features Overview

### Authentication
- JWT-based authentication
- Auto-redirect based on user role
- Protected routes with navigation guards

### Course Management
- Browse all published courses
- View detailed course information
- Enroll in courses (students)
- Create/edit/delete courses (instructors)

### Progress Tracking
- Track course completion percentage
- View enrolled courses
- Monitor learning progress

## Environment Variables

Create a `.env` file if you need to customize:
```
VITE_API_URL=http://localhost:8080/api
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

This project is part of the EduFlex educational platform.
