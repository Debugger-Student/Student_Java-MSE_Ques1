package com.student.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @Min(value = 0, message = "Programming score cannot be below 0")
    @Max(value = 100, message = "Programming score cannot exceed 100")
    private int programmingScore;

    @Min(value = 0, message = "Mathematics score cannot be below 0")
    @Max(value = 100, message = "Mathematics score cannot exceed 100")
    private int mathematicsScore;

    @ElementCollection
    @CollectionTable(
        name = "student_skills",
        joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "skill")
    private List<String> technicalSkills = new ArrayList<>();

    // JPA requires a no-argument constructor.
    public Student() {
    }

    // Parameterized constructor. The database generates studentId.
    public Student(String studentName, String email, String courseName,
                   int programmingScore, int mathematicsScore,
                   List<String> technicalSkills) {
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        setTechnicalSkills(technicalSkills);
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public int getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(int programmingScore) {
        this.programmingScore = programmingScore;
    }

    public int getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(int mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }

    // Return a copy so callers cannot directly change the internal list.
    public List<String> getTechnicalSkills() {
        return new ArrayList<>(technicalSkills);
    }

    public void setTechnicalSkills(List<String> technicalSkills) {
        this.technicalSkills = technicalSkills == null
                ? new ArrayList<>()
                : new ArrayList<>(technicalSkills);
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.isBlank()) {
            String cleanedSkill = skill.trim();
            boolean alreadyPresent = technicalSkills.stream()
                    .anyMatch(existing -> existing.equalsIgnoreCase(cleanedSkill));
            if (!alreadyPresent) {
                technicalSkills.add(cleanedSkill);
            }
        }
    }

    public double calculateAverage() {
        return (programmingScore + mathematicsScore) / 2.0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", email='" + email + '\'' +
                ", courseName='" + courseName + '\'' +
                ", programmingScore=" + programmingScore +
                ", mathematicsScore=" + mathematicsScore +
                ", technicalSkills=" + technicalSkills +
                '}';
    }
}
