package com.student.management.controller;

import com.student.management.entity.Student;
import com.student.management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // POST /students
    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        return service.addStudent(student);
    }

    // GET /students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // GET /students/{id}
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Integer id) {
        return service.getStudentById(id);
    }

    // PUT /students/{id}
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Integer id,
                                 @Valid @RequestBody Student student) {
        return service.updateStudent(id, student);
    }

    // DELETE /students/{id}
    @DeleteMapping("/{id}")
    public Map<String, String> deleteStudent(@PathVariable Integer id) {
        service.deleteStudent(id);
        return Map.of("message", "Student deleted successfully");
    }

    // GET /students/search/skill?skill=Java
    @GetMapping("/search/skill")
    public List<Student> searchBySkill(@RequestParam String skill) {
        return service.searchBySkill(skill);
    }

    // GET /students/search/course?courseName=B.Tech%20CSIT
    @GetMapping("/search/course")
    public List<Student> searchByCourse(@RequestParam String courseName) {
        return service.searchByCourse(courseName);
    }

    // GET /students/sort?order=asc or desc
    @GetMapping("/sort")
    public List<Student> sortStudents(
            @RequestParam(defaultValue = "desc") String order) {

        if (!order.equalsIgnoreCase("asc")
                && !order.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Order must be either 'asc' or 'desc'");
        }

        return service.sortByAverage(order.equalsIgnoreCase("desc"));
    }

    // GET /students/{id}/average
    @GetMapping("/{id}/average")
    public Map<String, Object> calculateAverage(@PathVariable Integer id) {
        return Map.of(
                "studentId", id,
                "average", service.calculateAverage(id)
        );
    }

    // POST /students/{id}/skills with body: {"skill":"Spring Boot"}
    @PostMapping("/{id}/skills")
    public Student addSkill(@PathVariable Integer id,
                            @RequestBody Map<String, String> request) {
        return service.addSkill(id, request.get("skill"));
    }

    // GET /students/count
    @GetMapping("/count")
    public Map<String, Long> countStudents() {
        return Map.of("totalStudents", service.countStudents());
    }
}
