![Screenshot](/public/assets/images/SS_THUMB.png)

> _Current version: 1.0_ \
> A miminalistic, modern tourism and local discovery platform for Sarajevo, built with Angular and designed for seamless exploration of the city's attractions, events, and local experiences.

**Live Demo:** Currently Unavailable | **Backend Repository:** [spotlight-sarajevo-be](https://github.com/username/spotlight-sarajevo-be) | **Design:** [Figma Prototype](https://figma.com/file/your-project)

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Design & Wireframes](#design--wireframes)
4. [Implementation](#implementation)
5. [Technical Overview](#technical-overview)
   - [Repository Structure](#repository-structure)
   - [Application Interfaces](#application-interfaces)
   - [Code Structure & Standards](#code-structure--standards)
   - [Future Improvements](#future-improvements)
6. [Other Information](#other-information)
7. [Relevant Links](#relevant-links)

---

## Overview

**Spotlight Sarajevo** is a comprehensive tourism and local discovery platform designed for both tourists and Sarajevo residents. The frontend is built with **Angular**, providing a modern, responsive user interface that enables seamless exploration of the city's rich cultural landscape.

### Key Purpose

The platform serves as a centralized hub for:
- **Discovering Local Attractions**: Browse and explore cafés, restaurants, historical sites, and cultural landmarks
- **Event Management**: Discover upcoming events, view past events, and participate in the city's vibrant cultural scene
- **Tourist Experiences**: Access curated tourist guides and local experiences
- **Public Transportation**: Navigate the city using integrated public transport information
- **Community Engagement**: Save favorite spots to personal collections, leave reviews and ratings, and share experiences with others

### Target Audience

- **Tourists**: First-time visitors to Sarajevo seeking comprehensive travel guidance
- **Locals**: City residents looking to discover new places and stay updated on local events

---

## Features

### Core User Features
- 🗺️ **Spot Discovery**: Browse, search, and filter cafés, restaurants, historical sites, and attractions
- 📅 **Event Management**: Explore upcoming and past events with detailed information
- 🎫 **Collections**: Create and manage personal collections of favorite spots and events
- ⭐ **Reviews & Ratings**: Leave detailed reviews and ratings for spots and event organizers
- 👥 **Tourist Guides**: Access professional tourist guides and local recommendations
- 🚌 **Transport Integration**: View public transportation routes and schedules
- 🏷️ **Categorization**: Intelligent filtering by category, tags, and location

### Administrative Features
- 📊 **Dashboard**: Comprehensive admin overview and analytics
- ✏️ **Content Management**: Create, edit, and manage spots, events, and guides
- 🔍 **Review Moderation**: Approve or reject user-submitted reviews

### Authentication & Authorization
- User registration and secure login
- Role-based access control (User, Moderator, Admin)
- Profile management and preferences

---

## Design & Wireframes

This project follows a **minimalistic design philosophy**, prioritizing usability, clarity, and intuitive navigation over decorative elements.

### Design Characteristics
- **Clean Interface**: Minimal visual clutter with focus on essential information
- **Consistent UX**: Uniform design patterns and component behavior across all pages
- **Accessibility First**: High contrast ratios, readable typography, and keyboard navigation
- **Responsive Layout**: Adapts seamlessly to all screen sizes

### View the Prototype

The complete design and interactive wireframes are available on Figma:
- **[Figma Design File - Spotlight Sarajevo](https://figma.com/file/your-project)**

The Figma prototype includes:
- User interface layouts for all major flows
- Admin dashboard mockups
- Mobile responsive previews
- Component library and design system specifications

---

## Implementation

### Technology Stack

**Frontend Framework:**
- Angular 19+ with standalone APIs
- TypeScript for type-safe development
- Angular CLI for build tooling

**Styling & UI:**
- CSS3 for responsive design
- Component-scoped styling
- CSS custom properties for theming

**Internationalization:**
- Transloco for multi-language support (English, Bosnian)
- Dynamic language switching

**Image Storage:**
- ImgBB for reliable, cloud-based image hosting

### Key Implementation Details

#### Responsive Web Application
- **Mobile-First Approach**: Designed and optimized for mobile devices first, then scaled to larger screens
- **Breakpoint Strategy**: Responsive breakpoints for mobile (< 768px), tablet (768-1024px), and desktop (> 1024px)
- **Touch Optimization**: Touch-friendly buttons and interactions for better mobile UX
- **Performance**: Optimized bundle size and lazy-loaded modules for faster load times

#### Architecture Highlights
- **Standalone Components**: Leveraging Angular's modern standalone component APIs
- **Modular Structure**: Well-organized feature modules with clear separation of concerns
- **Service-Based**: Centralized business logic through service layer
- **Interceptors**: Global HTTP interceptors for error handling and authentication
- **Guards**: Route guards for protecting authenticated and admin routes
- **Resolvers**: Data pre-fetching for smoother navigation

#### State Management
- Angular services with reactive programming patterns (RxJS)
- Observable-based data flow
- Proper subscription management to prevent memory leaks

---

## Technical Overview

### Repository Structure

```
spotlight-sarajevo-fe/
├── public/                     # Static assets
│   └── assets/
│       ├── fonts/             # Custom fonts
│       ├── i18n/              # Translation files (ba.json, en.json)
│       ├── icons/             # Icon assets
│       ├── images/            # Static images
│       └── markers/           # Map markers for events, spots, transport
├── src/
│   ├── index.html            # Main HTML entry point
│   ├── main.ts               # Angular bootstrap
│   ├── styles.css            # Global styles
│   └── app/
│       ├── app.ts            # Root component
│       ├── app.config.ts      # Angular configuration (providers)
│       ├── app.routes.ts      # Application routing
│       ├── transloco-loader.ts # i18n loader configuration
│       ├── components/        # Shared UI components
│       │   ├── admin-*        # Admin-specific components
│       │   ├── buttons/       # Button variants
│       │   ├── modals/        # Modal dialogs
│       │   ├── cards/         # Card components
│       │   ├── inputs/        # Form input components
│       │   └── ...
│       ├── core/              # Core module (singleton services)
│       │   ├── guards/        # Route guards
│       │   ├── interceptors/  # HTTP interceptors
│       │   ├── resolvers/     # Route resolvers
│       │   └── services/      # Core services
│       ├── pages/             # Feature pages (admin, auth, home)
│       ├── services/          # Domain-specific services
│       ├── interfaces/        # TypeScript interfaces & types
│       ├── shared/            # Shared utilities
│       │   ├── constants/     # Application constants
│       │   ├── models/        # Data models
│       │   ├── pipes/         # Custom pipes
│       │   └── utils/         # Utility functions
│       └── resources/         # Icons and resources
├── environments/              # Environment configurations
├── scripts/                   # Build scripts
├── angular.json              # Angular CLI configuration
├── tsconfig.json             # TypeScript configuration
├── package.json              # Dependencies
└── Dockerfile                # Docker configuration
```

### Application Interfaces

#### 1. **User Interface**
The public-facing interface designed for tourists and locals featuring:
- Home page with featured attractions
- Spot discovery and search with filtering
- Event browsing and details
- Collections management (save/remove favorites)
- Review and rating submission
- Tourist guide exploration
- User profile and preferences

**Key Routes:**
- `/` - Home
- `/spots` - Spot discovery
- `/events` - Event listing
- `/guides` - Tourist guides
- `/profile` - User profile
- `/auth/login` - Login
- `/auth/register` - Registration

#### 2. **Admin Interface**
Comprehensive administrative dashboard for content and user management:
- Admin overview and statistics
- Spot management (create, edit, review)
- Event management
- Tourist guide management
- Review moderation queue
- User activity monitoring
- Transport information management

**Key Routes:**
- `/admin` - Admin dashboard
- `/admin/spots` - Spot management
- `/admin/events` - Event management
- `/admin/reviews` - Review moderation
- `/admin/guides` - Guide management

#### 3. **Authentication Interface**
Secure user authentication and authorization:
- Login with email/password
- Registration for new users
- Role-based access control (User, Moderator, Admin)
- Session management
- Protected routes based on user roles

---

### Code Structure & Standards

#### Naming Conventions
- **Components**: `kebab-case.component.ts` (e.g., `spot-card.component.ts`)
- **Services**: `kebab-case.service.ts` (e.g., `spot.service.ts`)
- **Interfaces**: PascalCase in separate files (e.g., `ISpot.ts`)
- **Constants**: UPPER_SNAKE_CASE
- **Variables/Methods**: camelCase

#### File Organization
- **One component/service per file**: Provides clarity and follows Angular style guide
- **Related files co-located**: Component with its template and styles
- **Shared vs. Feature**: Clear boundaries between shared and feature-specific code
- **Barrel exports**: `index.ts` files for clean imports

#### Code Quality Standards
- **TypeScript**: Strict mode enabled for type safety
- **Linting**: Follow Angular ESLint rules
- **Testing**: Unit tests for services and critical components
- **Documentation**: JSDoc comments for public methods
- **Error Handling**: Proper error boundaries and user feedback
- **Performance**: OnPush change detection where applicable

#### Angular Best Practices
- **Standalone Components**: Preferred over NgModules for new components
- **Smart vs. Presentational**: Clear separation between container (smart) and presentational (dumb) components
- **RxJS**: Proper use of observables with proper unsubscribe patterns
- **Dependency Injection**: Constructor-based injection for dependencies
- **Lazy Loading**: Feature modules lazy-loaded where appropriate

---

### Future Improvements

#### 1. **Mobile Application**
- **Native Mobile App**: Development of iOS and Android applications using React Native or Flutter
- **Offline Support**: Enhanced offline functionality for viewing saved content
- **Push Notifications**: Real-time notifications for event updates and new recommendations
- **Native Features**: Integration with device camera for seamless image uploads

#### 2. **Advanced Features**
- **Real-Time Updates**: WebSocket integration for live event updates and notifications
- **AI Recommendations**: Machine learning-based spot and event recommendations
- **Social Features**: Follow users, share itineraries, social event creation
- **Advanced Analytics**: Enhanced admin analytics with data visualization
- **Booking Integration**: Direct booking integration for events and tours

#### 3. **Performance & Scalability**
- **PWA Features**: Progressive Web App capabilities for app-like experience
- **Caching Strategy**: Improved client-side caching and ServiceWorker optimization
- **Image Optimization**: Dynamic image resizing and format conversion
- **Database Optimization**: Query optimization and indexing strategies

#### 4. **Accessibility & Internationalization**
- **WCAG Compliance**: Full compliance with WCAG 2.1 AA standards
- **Additional Languages**: Support for more European languages
- **Screen Reader Support**: Enhanced accessibility for vision-impaired users

#### 5. **Developer Experience**
- **Storybook Integration**: Component documentation and visual testing
- **E2E Testing**: Comprehensive end-to-end testing with Playwright or Cypress
- **API Documentation**: Swagger/OpenAPI integration for API documentation
- **CI/CD Pipeline**: Automated testing and deployment workflows

---

## Other Information

### Image Storage
This project uses **ImgBB** for reliable and scalable cloud-based image hosting. ImgBB provides:
- Fast, CDN-backed image delivery
- Automatic image optimization
- Secure image management
- Free tier suitable for development and small-scale deployment

### Deployment Status
⚠️ **The current deployed version is temporarily unavailable.** The application is actively under development with new features and improvements being added regularly.

### Development Setup
1. Clone the repository
2. Install dependencies: `npm install`
3. Configure environment variables in `src/environments/`
4. Run development server: `npm start`
5. Access at `http://localhost:4200`

### Building for Production
```bash
npm run build
```

The build artifacts are stored in the `dist/` directory and are ready for deployment.

---

## Relevant Links

### Repositories
- **[Backend Repository](https://github.com/username/spotlight-sarajevo-be)** - Node.js/Express API server
- **[Frontend Repository](https://github.com/username/spotlight-sarajevo-fe)** - This repository

### Design & Prototypes
- **[Figma Design File](https://figma.com/file/your-project)** - Interactive wireframes and design system
- **[Project Documentation](link-to-design-docs)** - Project SRS & Documentation


---

**Last Updated**: March 2026 

