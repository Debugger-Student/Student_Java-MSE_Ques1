package com.student.management.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Student model.
 * Encapsulation: all fields are private and reached only through getters/setters.
 * Validation rules (0-100 scores, valid email ...) are declared on the fields.
 */
public class Student {

    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be a positive number")
    private Integer studentId;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotNull(message = "Programming score is required")
    @DecimalMin(value = "0.0", message = "Programming score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Programming score must be between 0 and 100")
    private Double programmingScore;

    @NotNull(message = "Mathematics score is required")
    @DecimalMin(value = "0.0", message = "Mathematics score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Mathematics score must be between 0 and 100")
    private Double mathematicsScore;

    @NotNull(message = "Technical skills list is required (it can be empty)")
    private List<String> technicalSkills = new ArrayList<>();

    // No-argument constructor (needed by Jackson and the RowMapper)
    public Student() {
    }

    // Parameterized constructor
    public Student(Integer studentId, String name, String email, String courseName,
                   Double programmingScore, Double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(Double programmingScore) {
        this.programmingScore = programmingScore;
    }

    public Double getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(Double mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    public List<String> getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(List<String> technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    // Two students are the same student if they have the same ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student other = (Student) o;
        return Objects.equals(studentId, other.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{id=" + studentId + ", name='" + name + "', email='" + email
                + "', course='" + courseName + "', programming=" + programmingScore
                + ", maths=" + mathematicsScore + ", skills=" + technicalSkills + "}";
    }
}
