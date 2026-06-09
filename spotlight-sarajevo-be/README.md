# Spotlight Sarajevo - Backend

> _Current version: 1.0_ \
> A robust RESTful API backend for the Spotlight Sarajevo platform, built with Spring Boot 3.5+ and designed to power seamless exploration of the city's attractions, events, and local experiences.

**API Documentation:** Available at `/swagger-ui.html` | **Frontend Repository:** [spotlight-sarajevo-fe](https://github.com/username/spotlight-sarajevo-fe) | **Design:** [Figma Prototype](https://figma.com/file/your-project)

---

## Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Technology Stack](#technology-stack)
4. [Technical Overview](#technical-overview)
    - [Repository Structure](#repository-structure)
    - [Architecture & Design Patterns](#architecture--design-patterns)
    - [Database Schema](#database-schema)
    - [API Endpoints](#api-endpoints)
    - [Code Structure & Standards](#code-structure--standards)
5. [Getting Started](#getting-started)
6. [Future Improvements](#future-improvements)
7. [Other Information](#other-information)
8. [Relevant Links](#relevant-links)

---

## Overview

**Spotlight Sarajevo Backend** is a comprehensive RESTful API built with **Spring Boot**, providing secure, scalable backend services for the Spotlight Sarajevo tourism discovery platform. The API handles all business logic, data persistence, authentication, and content management for both regular users and administrators.

### Key Purpose

The backend API serves as the central hub for:
- **Content Management**: Create, update, and delete attractions (spots), events, and tourist guides
- **User Authentication & Authorization**: Secure user registration, login, and role-based access control
- **Data Persistence**: Store and retrieve information about attractions, events, guides, and user interactions
- **Review Management**: Handle user reviews and ratings for attractions and event organizers
- **Collection Management**: Enable users to create and manage personal collections of favorite items
- **Media Management**: Handle image uploads, storage, and retrieval via cloud services
- **Administrative Controls**: Comprehensive admin dashboard APIs for content moderation and analytics

### Core Responsibilities

- RESTful API endpoints for all frontend operations
- JWT-based authentication and authorization
- PostgreSQL database with Flyway database migrations
- Integration with cloud-based image storage (ImgBB)
- Comprehensive error handling and validation
- OpenAPI/Swagger documentation

---

## Key Features

### Core API Capabilities
- 📍 **Spot Management API**: Full CRUD operations for attractions with location-based querying
- 📅 **Event Management API**: Create, update, delete, and query events with date-based filtering
- 📚 **Tourist Guide Management**: Manage comprehensive guides with sections and multilingual content
- ⭐ **Review & Rating System**: Submit, retrieve, and manage reviews with user authentication
- 🎫 **Collections API**: Create and manage personal collections of favorite spots and events
- 🏷️ **Categorization & Tagging**: Intelligent filtering by category and tags
- 🖼️ **Media Management**: Handle image uploads, storage, and metadata management

### Authentication & Security
- 🔐 **JWT Authentication**: Secure token-based authentication with refresh token support
- 👥 **Role-Based Access Control**: Three-tier user hierarchy (User, Moderator, Admin)
- 🔒 **OAuth2 Integration**: Google OAuth2 authentication support
- 🛡️ **Request Validation**: Comprehensive input validation and sanitization
- 🔑 **Principal-Based Authorization**: User-specific data isolation and access control

### Administrative Features
- 📊 **Admin Dashboard APIs**: Endpoints for statistics and overview data
- ✏️ **Content Moderation**: Review approval/rejection workflows
- 📈 **Analytics & Reporting**: Statistical endpoints for admin insights
- 🗑️ **Bulk Operations**: Efficient deletion and management of multiple resources
- 📋 **Community Request Management**: Handle and process community requests

### Data & Performance
- 🗄️ **PostgreSQL Database**: Robust relational database with full-text search capabilities
- 📍 **Geospatial Queries**: Location-based filtering using JTS/PostGIS
- 🔄 **Database Migrations**: Flyway-managed schema versioning and evolution
- 🚀 **Performance Optimized**: Efficient querying with JPA specifications and custom queries
- 💾 **Transaction Management**: ACID compliance for data integrity

---

## Technology Stack

### Core Framework
- **Spring Boot 3.5.6**: Latest Spring Boot framework for rapid development
- **Java 21**: Modern Java with latest features and performance improvements
- **Spring Security**: Enterprise-grade security framework with JWT support
- **Spring Data JPA**: Simplified data access layer with ORM capabilities

### Database & Persistence
- **PostgreSQL 16+**: Enterprise-grade relational database
- **Hibernate ORM**: Object-relational mapping with custom annotations
- **Flyway**: Database migration and versioning tool
- **JTS (Java Topology Suite)**: Geospatial data support for location-based queries
- **Hibernate Spatial**: PostGIS integration for geographic queries

### API & Integration
- **Spring Web**: RESTful API development framework
- **SpringDoc OpenAPI**: Automatic Swagger/OpenAPI documentation generation
- **JWT (JJWT)**: JSON Web Token library for secure authentication
- **Google API Client**: Google OAuth2 and Maps API integration
- **Jackson**: JSON processing and serialization

### Development Tools
- **MapStruct**: Code generation-based object mapping (no reflection overhead)
- **Lombok**: Annotation processing for boilerplate reduction
- **Spring Validation**: Bean validation with Hibernate Validator
- **Maven**: Dependency management and build automation

---

## Technical Overview

### Repository Structure

```
spotlight-sarajevo-be/
├── src/
│   ├── main/
│   │   ├── java/com/spotlightsarajevo/
│   │   │   ├── SpotlightSarajevoApplication.java    # Spring Boot entry point
│   │   │   ├── common/                              # Shared utilities & configurations
│   │   │   │   ├── enums/                          # Application-wide enumerations
│   │   │   │   │   ├── ObjectType.java             # Media/object categorization
│   │   │   │   │   ├── UserRole.java               # User role definitions
│   │   │   │   │   └── ...
│   │   │   │   ├── exceptions/                     # Custom exception classes
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   ├── UnauthorizedException.java
│   │   │   │   │   └── ...
│   │   │   │   ├── specifications/                 # JPA Specifications for advanced queries
│   │   │   │   │   ├── SpotSpecification.java
│   │   │   │   │   ├── EventSpecification.java
│   │   │   │   │   └── ...
│   │   │   │   └── utils/                          # Utility classes
│   │   │   │       ├── SlugUtil.java
│   │   │   │       └── ...
│   │   │   ├── config/                              # Application configuration
│   │   │   │   ├── SecurityConfig.java             # Spring Security configuration
│   │   │   │   ├── CorsConfig.java                 # CORS configuration
│   │   │   │   ├── JwtProperties.java              # JWT configuration properties
│   │   │   │   └── ...
│   │   │   └── modules/                             # Feature modules (domain-driven)
│   │   │       ├── auth/                           # Authentication module
│   │   │       │   ├── api/                        # REST controllers
│   │   │       │   │   └── AuthRestController.java
│   │   │       │   ├── domain/                     # JPA entities
│   │   │       │   │   └── User.java
│   │   │       │   ├── service/                    # Business logic
│   │   │       │   │   └── AuthService.java
│   │   │       │   └── mapper/                     # DTOs & mappers
│   │   │       │       └── UserMapper.java
│   │   │       ├── spot/                           # Spot/attraction module
│   │   │       │   ├── api/
│   │   │       │   │   └── SpotRestController.java
│   │   │       │   ├── domain/
│   │   │       │   │   ├── Spot.java
│   │   │       │   │   ├── SpotWorkHours.java
│   │   │       │   │   └── ...
│   │   │       │   ├── service/
│   │   │       │   │   ├── SpotService.java
│   │   │       │   │   └── SpotUtilities.java
│   │   │       │   ├── mapper/
│   │   │       │   │   └── SpotMapper.java
│   │   │       │   └── facades/ (if applicable)
│   │   │       ├── event/                          # Event management module
│   │   │       │   ├── api/
│   │   │       │   ├── domain/
│   │   │       │   ├── service/
│   │   │       │   └── mapper/
│   │   │       ├── guide/                          # Tourist guide module
│   │   │       │   ├── api/
│   │   │       │   ├── domain/
│   │   │       │   ├── service/
│   │   │       │   └── mapper/
│   │   │       ├── media/                          # Media management module
│   │   │       │   ├── api/
│   │   │       │   ├── domain/
│   │   │       │   ├── service/
│   │   │       │   │   ├── MediaService.java      # Media operations
│   │   │       │   │   └── MediaUtilities.java    # Media utility methods
│   │   │       │   └── mapper/
│   │   │       ├── review/                         # Review & rating module
│   │   │       ├── collection/                     # User collections module
│   │   │       ├── category/                       # Category management
│   │   │       ├── tag/                            # Tag management
│   │   │       ├── community/                      # Community requests module
│   │   │       └── transport/                      # Public transport info
│   │   │
│   │   └── resources/
│   │       ├── application.yaml                   # Default configuration
│   │       ├── application-dev.yaml               # Development profile
│   │       ├── application-prod.yaml              # Production profile
│   │       ├── db/
│   │       │   └── migration/                     # Flyway SQL migrations
│   │       │       ├── V1__initial_schema.sql
│   │       │       ├── V2__constant_data_seed.sql
│   │       │       └── V{n}__*.sql                # Sequential migrations
│   │       └── i18n/ (if applicable)              # Internationalization resources
│   │
│   └── test/
│       └── java/com/spotlightsarajevo/           # Unit and integration tests
│
├── target/                                         # Build artifacts (generated)
├── pom.xml                                        # Maven configuration & dependencies
├── mvnw, mvnw.cmd                                 # Maven wrapper scripts
├── Dockerfile                                     # Docker containerization
└── README.md                                      # This file
```

### Architecture & Design Patterns

#### Layered Architecture
The application follows a **three-tier layered architecture**:

1. **API Layer (REST Controllers)**
   - `@RestController` classes handling HTTP requests/responses
   - Request validation and parameter binding
   - HTTP status code management
   - Located in `modules/*/api/`

2. **Service Layer (Business Logic)**
   - `@Service` classes containing core business logic
   - Transaction management with `@Transactional`
   - Data validation and transformation
   - Located in `modules/*/service/`

3. **Data Layer (JPA Entities & Repositories)**
   - `@Entity` classes mapping to database tables
   - `JpaRepository` interfaces for CRUD operations
   - Query methods and custom specifications
   - Located in `modules/*/domain/`

#### Module-Based Organization
Each feature is organized as a self-contained module:
- **auth**: User authentication and authorization
- **spot**: Attraction/destination management
- **event**: Event lifecycle management
- **guide**: Tourist guide content
- **media**: Image and file management
- **review**: Review and rating system
- **collection**: User-created collections
- **category**, **tag**: Metadata management
- **community**: Community request handling
- **transport**: Public transportation data

#### Design Patterns Used

**Mapper Pattern (MapStruct)**
- Entity → DTO mapping with code generation
- Type-safe, compile-time checking
- Zero-reflection overhead
- Located in `mapper/` folders with `@Mapper` interface definitions

**Specification Pattern (Spring Data JPA)**
- Reusable predicates for complex queries
- Replaces scattered `@Query` annotations
- Database-agnostic query building
- Located in `common/specifications/`

**Service Locator Pattern**
- Centralized business logic access
- Dependency injection via constructors
- Cross-cutting concerns (logging, transactions)

**Utility Classes**
- Helper methods for common operations
- `*Utilities.java` classes for domain-specific utilities
- `MediaUtilities` for image operations
- `EventUtilities` for event-specific logic

#### Data Validation
- **Bean Validation**: `@NotNull`, `@NotBlank`, `@Size` annotations on DTOs
- **Custom Validators**: Reusable validation logic
- **Method-level Validation**: Service layer input validation
- **Database Constraints**: Additional integrity checks in schema

### Database Schema

The PostgreSQL database uses **Flyway** for version-controlled migrations. Key entities include:

- **User**: Authentication and profile data with role-based access
- **Spot**: Attractions/destinations with geospatial coordinates
- **Event**: Time-based events linked to organizers
- **TouristGuide**: Content guides with multilingual support and sections
- **Review**: User-submitted reviews for spots and organizers
- **Collection**: User-created collections of favorite items
- **MediaStore**: Image metadata and cloud storage references
- **Category**, **Tag**: Metadata for filtering and organization
- **CommunityRequest**: User-submitted suggestions and requests

**Key Features:**
- Multilingual support with `_bs` and `_en` suffixes for Bosnian/English
- Slug-based URLs for SEO-friendly endpoints
- Geospatial data with PostGIS/JTS integration
- Cascade delete for related entities
- Proper indexing for performance optimization

### API Endpoints

#### Authentication Endpoints (`/auth`)
- `POST /auth/register` - User registration
- `POST /auth/login` - User login with JWT
- `POST /auth/refresh-token` - Refresh JWT token
- `POST /auth/logout` - User logout

#### Spot Endpoints (`/spot`)
- `GET /spot` - List all spots with filtering
- `POST /spot` - Create new spot (Admin)
- `GET /spot/{slugOrId}` - Get spot details
- `PUT /spot/{id}` - Update spot (Admin)
- `DELETE /spot/{id}` - Delete spot (Admin)
- `POST /spot/{id}/review` - Submit review
- `GET /spot/admin/count` - Get total spot count (Admin)
- `GET /spot/admin/recently-added` - Recent spots (Admin)

#### Event Endpoints (`/event`)
- `GET /event` - List all events with filtering
- `POST /event` - Create new event (Admin)
- `GET /event/{slugOrId}` - Get event details
- `PUT /event/{id}` - Update event (Admin)
- `DELETE /event/{id}` - Delete event (Admin)
- `GET /event/admin/count` - Get total event count (Admin)
- `GET /event/admin/recently-added` - Recent events (Admin)

#### Tourist Guide Endpoints (`/guide`)
- `GET /guide` - List all guides with filtering
- `POST /guide` - Create new guide (Admin)
- `GET /guide/{slugOrId}` - Get guide details
- `PUT /guide/{id}` - Update guide (Admin)
- `DELETE /guide/{id}` - Delete guide (Admin)
- `GET /guide/{guideId}/section/{sectionId}` - Get guide section

#### Collection Endpoints (`/collection`)
- `GET /collection` - List user's collections
- `POST /collection` - Create new collection
- `POST /collection/{id}/add/{itemType}/{itemId}` - Add item to collection
- `DELETE /collection/{id}/remove/{itemType}/{itemId}` - Remove item

#### Review Endpoints (`/review`)
- `GET /review/{entityType}/{entityId}` - Get reviews for entity
- `POST /review` - Submit new review
- `GET /review/admin/pending` - Get pending reviews (Admin)
- `PUT /review/{id}/approve` - Approve review (Admin)
- `DELETE /review/{id}/reject` - Reject review (Admin)

#### Admin Endpoints (`/admin`)
- `GET /admin/dashboard` - Admin dashboard data
- `GET /admin/community-requests` - Community requests management
- `GET /admin/get-all-requests` - All requests with filtering
- `GET /admin/analytics` - Various analytics endpoints

Complete API documentation available at `/swagger-ui.html` when application is running.

### Code Structure & Standards

#### Naming Conventions
- **Classes**: PascalCase (e.g., `SpotRestController`, `SpotService`)
- **Variables/Methods**: camelCase (e.g., `spotName`, `createSpot()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_PAGE_SIZE`, `DEFAULT_LIMIT`)
- **Enums**: PascalCase (e.g., `ObjectType`, `UserRole`)
- **Files**: Match class name exactly

#### Class Organization
- **One public class per file**: Ensures clarity and modularity
- **Imports**: Organized and non-redundant
- **Related code**: Grouped by functionality
- **Method ordering**: Public → Protected → Private, Getters/Setters at end

#### Spring Annotations Best Practices
- **Dependency Injection**: Constructor-based (preferred over field injection)
- **Transactions**: `@Transactional` on service layer methods
- **Validation**: `@Valid` on controller parameters
- **Caching**: Strategic caching for frequently accessed data
- **Profiles**: `@Profile("dev")`, `@Profile("prod")` for environment-specific beans

#### Entity Design
- **ID Generation**: Auto-generated primary keys with `@GeneratedValue`
- **Relationships**: Explicit `@ManyToOne`, `@OneToMany`, `@ManyToMany` annotations
- **Cascade**: Appropriate cascade types to prevent orphaned records
- **Indexes**: Strategic `@Index` annotations for query performance
- **Auditing**: Timestamps for `createdAt`, `updatedAt` (if applicable)

#### DTO/Model Design
- **Request DTOs**: For incoming client data (Create/Update models)
- **Response DTOs**: For outgoing API responses
- **Validation**: `@NotNull`, `@NotBlank`, `@Size` on DTO fields
- **Immutability**: Prefer record classes or constructor-based models
- **Separation**: Distinct models for create, update, and response operations

#### Service Layer Patterns
- **Single Responsibility**: Each service handles one domain
- **Utilities**: Domain-specific utilities in `*Utilities` classes
- **Transaction Management**: `@Transactional` with appropriate propagation
- **Exception Handling**: Throw custom exceptions for business logic errors
- **Dependency Injection**: Constructor injection of repositories and other services

#### Query Best Practices
- **Specifications**: Use Spring Data JPA Specifications for complex queries
- **Lazy Loading**: Be aware of N+1 query problems
- **Custom Queries**: Use `@Query` only when specifications are insufficient
- **Pagination**: Implement pagination for large result sets
- **Sorting**: Always allow sorting by relevant fields


### Environment Profiles

The application supports multiple profiles:

- **dev** (`application-dev.yaml`): Development environment with debug logging
- **prod** (`application-prod.yaml`): Production environment with optimized settings
- **default** (`application.yaml`): Fallback configuration


### Short-term Enhancements
- **Caching Layer**: Redis integration for frequently accessed data
- **Advanced Analytics**: More granular usage statistics and insights
- **Batch Operations**: Bulk import/export functionality
- **Full-Text Search**: Enhanced search capabilities with Elasticsearch
- **Rate Limiting**: API throttling to prevent abuse

### Medium-term Features
- **Real-Time Notifications**: WebSocket support for live updates
- **Recommendation Engine**: ML-based recommendations for users
- **Advanced Filtering**: More sophisticated query capabilities
- **Media Processing**: Automatic image optimization and resizing
- **Event Ticketing**: Integrated booking and ticket management

### Long-term Vision
- **Mobile API Optimization**: Dedicated mobile endpoints
- **GraphQL**: Alternative query language alongside REST
- **Microservices**: Potential refactoring into microservices architecture
- **Machine Learning**: Predictive analytics and personalization
- **Internationalization**: Support for additional languages and locales

---

## Other Information

### Image Storage & Media Management
This project uses **ImgBB** for reliable and scalable cloud-based image hosting. ImgBB provides:
- Fast, CDN-backed image delivery
- Automatic image optimization
- Secure image management
- Free tier suitable for development and small-scale deployment

The `MediaService` and `MediaUtilities` classes handle all image operations:
- Image upload to ImgBB
- Metadata storage in PostgreSQL
- Image deletion and lifecycle management
- Thumbnail and gallery image handling

---

## Relevant Links

### Repositories
- **[Frontend Repository](https://github.com/username/spotlight-sarajevo-fe)** - Angular frontend
- **[Backend Repository](https://github.com/username/spotlight-sarajevo-be)** - This repository

### Design & Documentation
- **[Figma Design File](https://figma.com/file/your-project)** - Interactive wireframes and design system
- **[Project Documentation](link-to-design-docs)** - Project SRS & Documentation
- **[API Documentation (Live)](http://localhost:8080/swagger-ui.html)** - Available when app is running

### Technology Resources
- **[Spring Boot Documentation](https://spring.io/projects/spring-boot)**
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**
- **[Spring Security](https://spring.io/projects/spring-security)**
- **[MapStruct Documentation](https://mapstruct.org/)**
- **[Flyway Database Migrations](https://flywaydb.org/)**
- **[PostgreSQL Documentation](https://www.postgresql.org/docs/)**
- **[JWT.io](https://jwt.io/)**

### External Services
- **[ImgBB API](https://imgbb.com/api)** - Image hosting
- **[Google OAuth2](https://developers.google.com/identity/protocols/oauth2)** - Authentication
- **[Google Maps API](https://developers.google.com/maps)** - Location services

---

**Last Updated**: March 2026 

