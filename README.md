# Student Profile and Skill Development Management System

A Spring Boot + MySQL REST API project demonstrating Java OOP, Java Collections,
business logic, validation, and database CRUD operations.

## Requirements

- JDK 17 or later
- Maven (or use the Maven wrapper if you add one)
- MySQL Server
- IntelliJ IDEA, Eclipse, or VS Code
- Postman (optional, for API testing)

## 1. Create the database

Open MySQL Workbench and run:

```sql
CREATE DATABASE student_db;
```

Hibernate creates the `students` and `student_skills` tables automatically.

## 2. Configure your MySQL password

Open:

`src/main/resources/application.properties`

Change:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Use your actual MySQL username and password.

## 3. Run the application

Open this folder as a Maven project in your IDE.

Run:

`src/main/java/com/student/management/StudentManagementApplication.java`

Or from a terminal in the project root:

```bash
mvn spring-boot:run
```

The application runs at:

`http://localhost:8080`

## 4. API endpoints

| Method | URL | Purpose |
|---|---|---|
| POST | `/students` | Add a student |
| GET | `/students` | Get all students |
| GET | `/students/{id}` | Get student by ID |
| PUT | `/students/{id}` | Update a student |
| DELETE | `/students/{id}` | Delete a student |
| GET | `/students/search/skill?skill=Java` | Search by skill |
| GET | `/students/search/course?courseName=B.Tech%20CSIT` | Search by course |
| GET | `/students/sort?order=asc` | Sort by average ascending |
| GET | `/students/sort?order=desc` | Sort by average descending |
| GET | `/students/{id}/average` | Calculate average |
| POST | `/students/{id}/skills` | Add a skill |
| GET | `/students/count` | Count students |

## 5. Sample POST request

Method: `POST`

URL: `http://localhost:8080/students`

Body -> raw -> JSON:

```json
{
  "studentName": "Rahul Sharma",
  "email": "rahul@gmail.com",
  "courseName": "B.Tech CSIT",
  "programmingScore": 90,
  "mathematicsScore": 85,
  "technicalSkills": ["Java", "Python", "SQL"]
}
```

Do not send `studentId` when adding a student; MySQL generates it.

## 6. Add a skill

Method: `POST`

URL: `http://localhost:8080/students/1/skills`

Body:

```json
{
  "skill": "Spring Boot"
}
```

## 7. Validation rules

- Student name, email and course name cannot be blank.
- Email must have a valid format.
- Programming score must be between 0 and 100.
- Mathematics score must be between 0 and 100.
- Email must be unique.
- A missing student ID returns HTTP 404.
- Invalid request data returns HTTP 400.

## Project package structure

```text
student-management/
├── pom.xml
├── README.md
├── database/
│   └── create_database.sql
└── src/
    └── main/
        ├── java/com/student/management/
        │   ├── StudentManagementApplication.java
        │   ├── controller/StudentController.java
        │   ├── entity/Student.java
        │   ├── exception/GlobalExceptionHandler.java
        │   ├── exception/ResourceNotFoundException.java
        │   ├── repository/StudentRepository.java
        │   └── service/StudentService.java
        └── resources/
            └── application.properties
```

## Notes

- `Student` is a JPA entity. `@ElementCollection` stores skills in a separate table.
- `StudentRepository` uses Spring Data JPA.
- `StudentService` contains business logic and Java Collections sorting.
- `StudentController` exposes REST endpoints.
- `GlobalExceptionHandler` returns readable error responses.
