# Job Hunter API - Backend

## Overview
Job Hunter API is a backend service built using **Spring Boot**, designed to manage job postings, applications, and user profiles efficiently. The API supports features like job searching, applying to jobs, user authentication, and employer management.

## Features
- **User Authentication & Authorization** (JWT-based, Access Token & Refresh Token)
- **Role & Permission Management**
- **Job Posting Management** (Create, Update, Delete, Search)
- **Job Application System** (Apply, Track Status)
- **User Profiles** (Job Seeker & Employer Management)
- **Company Management** (Create & Manage Companies)
- **Resume Management** (Upload & Manage Resumes)
- **Subscribers & Email Notifications** (Send job updates via email)
- **Admin Dashboard** (Manage Users, Jobs & Companies)
- **Skill Management** (Assign Skills to Jobs)
- **RESTful API Architecture**
- **Database Integration** (MySQL)
- **Swagger API Documentation**

## Tech Stack
- **Spring Boot** (REST API)
- **Spring Security** (JWT Authentication, Role & Permission)
- **Hibernate/JPA** (Database ORM)
- **MySQL/PostgreSQL** (Database)
- **Swagger** (API Documentation)
- **Maven** (Dependency Management)
- **Docker** (Containerization)

## Installation
### Prerequisites
- Java 17+
- Maven 3+
- MySQL
- Docker (Optional)

### Clone the repository
```sh
$ git clone https://github.com/your-repo/job-hunter-api.git
$ cd job-hunter-api
```

### Configure Database
Update `application.properties` or `application.yml`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/job_hunter_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### Build & Run
#### Using Maven
```sh
$ mvn clean install
$ mvn spring-boot:run
```

#### Using Docker
```sh
$ docker-compose up --build
```

## API Endpoints
### Authentication
| Method | Endpoint                     | Description         |
|--------|------------------------------|---------------------|
| POST   | `/api/v1/auth/signup`        | User Registration  |
| POST   | `/api/v1/auth/login`         | User Login         |
| POST   | `/api/v1/auth/refresh`       | Refresh Access Token |

### User & Role Management
| Method | Endpoint                     | Description        |
|--------|------------------------------|--------------------|
| GET    | `/api/v1/users`              | List all users    |
| POST   | `/api/v1/users`              | Create user       |
| GET    | `/api/v1/users/{id}`         | Get user details  |
| PUT    | `/api/v1/users/{id}`         | Update user       |
| DELETE | `/api/v1/users/{id}`         | Delete user       |
| GET    | `/api/v1/roles`              | List roles        |
| POST   | `/api/v1/roles`              | Create role       |
| PUT    | `/api/v1/roles/{id}`         | Update role       |
| DELETE | `/api/v1/roles/{id}`         | Delete role       |

### Jobs Management
| Method | Endpoint                     | Description        |
|--------|------------------------------|--------------------|
| GET    | `/api/v1/jobs`               | List all jobs     |
| POST   | `/api/v1/jobs`               | Create job post   |
| GET    | `/api/v1/jobs/{id}`          | Get job details   |
| PUT    | `/api/v1/jobs/{id}`          | Update job post   |
| DELETE | `/api/v1/jobs/{id}`          | Delete job post   |

### Skill Management
| Method | Endpoint                     | Description        |
|--------|------------------------------|--------------------|
| GET    | `/api/v1/skills`             | List all skills   |
| POST   | `/api/v1/skills`             | Create skill      |
| GET    | `/api/v1/skills/{id}`        | Get skill details |
| PUT    | `/api/v1/skills/{id}`        | Update skill      |
| DELETE | `/api/v1/skills/{id}`        | Delete skill      |
| POST   | `/api/v1/jobs/{jobId}/skills/{skillId}` | Assign skill to job |
| DELETE | `/api/v1/jobs/{jobId}/skills/{skillId}` | Remove skill from job |

### Applications
| Method | Endpoint                     | Description          |
|--------|------------------------------|----------------------|
| GET    | `/api/v1/applications`       | View applications   |
| POST   | `/api/v1/applications`       | Apply to a job      |
| GET    | `/api/v1/applications/{id}`  | Get application     |
| PUT    | `/api/v1/applications/{id}`  | Update application  |
| DELETE | `/api/v1/applications/{id}`  | Delete application  |

### Company Management
| Method | Endpoint                     | Description          |
|--------|------------------------------|----------------------|
| GET    | `/api/v1/companies`          | List all companies  |
| POST   | `/api/v1/companies`          | Create company      |
| GET    | `/api/v1/companies/{id}`     | Get company details |
| PUT    | `/api/v1/companies/{id}`     | Update company      |
| DELETE | `/api/v1/companies/{id}`     | Delete company      |

### Resume Management
| Method | Endpoint                     | Description            |
|--------|------------------------------|------------------------|
| GET    | `/api/v1/resumes`            | List all resumes      |
| POST   | `/api/v1/resumes`            | Upload resume         |
| GET    | `/api/v1/resumes/{id}`       | View resume details   |
| PUT    | `/api/v1/resumes/{id}`       | Update resume         |
| DELETE | `/api/v1/resumes/{id}`       | Delete resume         |

### Subscribers & Email Notifications
| Method | Endpoint                     | Description              |
|--------|------------------------------|--------------------------|
| GET    | `/api/v1/subscribers`        | List all subscribers    |
| POST   | `/api/v1/subscribers`        | Subscribe to job updates |
| DELETE | `/api/v1/subscribers/{id}`   | Unsubscribe             |
| POST   | `/api/v1/email/send`         | Send job email alerts    |

## API Documentation
Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

## Contributing
1. Fork the repository
2. Create a new branch (`feature-xyz`)
3. Commit your changes (`git commit -m "Added new feature"`)
4. Push to the branch (`git push origin feature-xyz`)
5. Create a Pull Request

## License
This project is licensed under the MIT License.

