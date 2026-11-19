package com.student.management.dto;

import java.util.List;

/** What the API sends back: the student plus the calculated values. */
public record StudentResponse(
        Integer studentId,
        String name,
        String email,
        String courseName,
        Double programmingScore,
        Double mathematicsScore,
        List<String> technicalSkills,
        Double averageScore,
        String performanceCategory,
        String strongerSubject) {
}
