package com.student.management.dto;

/** One row of the merit list. */
public record MeritListEntry(
        int rank,
        Integer studentId,
        String name,
        String courseName,
        Double averageScore,
        String performanceCategory) {
}
