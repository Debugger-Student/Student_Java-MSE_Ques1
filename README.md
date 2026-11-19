# Student Profile and Skill Development Management System

OOPJ (CS336B) MSE-II case study. Spring Boot 3.4.5 REST API with **JDBC (JdbcTemplate)** and **MySQL**.

## 1. Run it

1. Install Java 17+ and MySQL 8.0.16+ (CHECK constraints need 8.0.16 or newer).
2. Open `src/main/resources/application.properties` and put your MySQL password in
   `spring.datasource.password` (or set the `DB_PASSWORD` environment variable).
3. From this folder:
   ```
   mvn spring-boot:run
   ```
   The database `student_db` and both tables are created automatically.
   (To create them by hand, run `database/create_database.sql` in MySQL Workbench.)
4. Import `postman/Student-Management.postman_collection.json` into Postman and run folders 1 to 8 in order.

If Maven fails with `HTTP Status: 429`, add a mirror in `C:\Users\<you>\.m2\settings.xml`
or switch to a mobile hotspot.

## 2. Endpoints (base URL `http://localhost:8080`)

| # | Requirement | Method and URL | Success |
|---|---|---|---|
| 1 | Add a student | `POST /api/students` | 201 |
| 2 | Retrieve all students | `GET /api/students` | 200 |
| 3 | Search a student by ID | `GET /api/students/{id}` | 200 |
| 4 | Update scores and skills | `PUT /api/students/{id}` | 200 |
| 5 | Delete a student | `DELETE /api/students/{id}` | 200 |
| 6 | Merit list | `GET /api/students/merit-list` | 200 |
| extra | Search by email or course | `GET /api/students/search?email=...` or `?course=...` | 200 |
| extra | Students with the same skills | `GET /api/students/same-skills` | 200 |
| extra | Programming below 50, maths 75 or more | `GET /api/students/needs-programming-support` | 200 |

Errors: `400` validation/bad input, `404` student not found, `409` duplicate ID or email.

Sample POST body:
```json
{
  "studentId": 101,
  "name": "Aarav Sharma",
  "email": "aarav.sharma@kiet.edu",
  "courseName": "B.Tech CSIT",
  "programmingScore": 92,
  "mathematicsScore": 88,
  "technicalSkills": ["Java", "MySQL", "Spring Boot"]
}
```

## 3. Business rules and where they are

| Rule | Where |
|---|---|
| Average = (programming + maths) / 2, rounded to 2 decimals | `StudentService.average()` |
| 85+ Excellent, 70-84.99 Good, 50-69.99 Satisfactory, below 50 Needs Improvement | `StudentService.category()` |
| Stronger subject (`Both Equal` on a tie) | `StudentService.strongerSubject()` |
| Merit list: average high to low, then programming score, then name | `MERIT_ORDER` comparator |
| Same technical skills | `StudentService.getSameSkillGroups()` |
| Programming below 50 and maths 75 or more | `getNeedsProgrammingSupport()` |
| No duplicate IDs | `existsById()` check in the service, plus `PRIMARY KEY` in MySQL |
| Scores between 0 and 100 | `@DecimalMin/@DecimalMax` on the model, plus `CHECK` in MySQL |

Assumptions: "needs improvement in programming" means a programming score below 50.
"Same technical skills" is shown two ways (students sharing each skill, and students with an identical skill set).

## 4. Collections used (for the viva)

| Collection | Where | Why |
|---|---|---|
| `List<String>` | `Student.technicalSkills` | ordered skills of one student, easy to loop |
| `List<Student>` | repository and service results | a group of students, can be sorted |
| `Map<Integer, List<String>>` (`HashMap`) | `StudentRepository.attachSkills()` | all skills loaded in one query, then matched to students by ID |
| `Map<String, String>` (`LinkedHashMap`) | `StudentRepository.saveSkills()` | removes duplicate skills (Java / java) and keeps the order |
| `Set<String>` (`TreeSet`) | `getSameSkillGroups()` | a student's skills without duplicates, sorted |
| `Map<String, List<String>>` (`TreeMap`) | `getSameSkillGroups()` | skill -> students who have it, sorted by skill |
| `Comparator<Student>` | `MERIT_ORDER` | defines the merit list order |
| Streams (`filter`, `map`, `collect`) | service | filtering and converting lists |

## 5. Project structure

```
student-management
|-- pom.xml
|-- database/create_database.sql
|-- postman/Student-Management.postman_collection.json
`-- src/main
    |-- java/com/student/management
    |   |-- StudentManagementApplication.java
    |   |-- model/Student.java
    |   |-- dto/            StudentUpdateRequest, StudentResponse, MeritListEntry
    |   |-- repository/StudentRepository.java      (JdbcTemplate + SQL)
    |   |-- service/StudentService.java            (business rules)
    |   |-- controller/StudentController.java      (REST endpoints)
    |   `-- exception/      not-found, duplicate, global handler
    `-- resources/          application.properties, schema.sql
```
