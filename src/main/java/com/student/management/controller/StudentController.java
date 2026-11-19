package com.student.management.controller;

import com.student.management.dto.MeritListEntry;
import com.student.management.dto.StudentResponse;
import com.student.management.dto.StudentUpdateRequest;
import com.student.management.model.Student;
import com.student.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // 1. Add a student            POST   /api/students
    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStudent(student));
    }

    // 2. Retrieve all students    GET    /api/students
    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return service.getAllStudents();
    }

    // 3. Search a student by ID   GET    /api/students/{id}
    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable("id") int id) {
        return service.getStudentById(id);
    }

    // 4. Update scores and skills PUT    /api/students/{id}
    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable("id") int id,
                                         @Valid @RequestBody StudentUpdateRequest request) {
        return service.updateStudent(id, request);
    }

    // 5. Delete a student record  DELETE /api/students/{id}
    @DeleteMapping("/{id}")
    public Map<String, String> deleteStudent(@PathVariable("id") int id) {
        service.deleteStudent(id);
        return Map.of("message", "Student with ID " + id + " deleted successfully");
    }

    // 6. Display the merit list   GET    /api/students/merit-list
    @GetMapping("/merit-list")
    public List<MeritListEntry> getMeritList() {
        return service.getMeritList();
    }

    // ---- Extra endpoints that show the business rules of the problem statement ----

    // Search by email or course   GET    /api/students/search?email=...  or  ?course=...
    @GetMapping("/search")
    public List<StudentResponse> search(@RequestParam(name = "email", required = false) String email,
                                        @RequestParam(name = "course", required = false) String course) {
        return service.search(email, course);
    }

    // Students with the same technical skills
    @GetMapping("/same-skills")
    public Map<String, Object> getSameSkillGroups() {
        return service.getSameSkillGroups();
    }

    // Programming below 50 but mathematics 75 or more
    @GetMapping("/needs-programming-support")
    public List<StudentResponse> getNeedsProgrammingSupport() {
        return service.getNeedsProgrammingSupport();
    }
}
