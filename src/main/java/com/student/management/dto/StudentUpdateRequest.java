package com.student.management.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/** Body of PUT /api/students/{id}: only scores and skills can be updated. */
public class StudentUpdateRequest {

    @NotNull(message = "Programming score is required")
    @DecimalMin(value = "0.0", message = "Programming score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Programming score must be between 0 and 100")
    private Double programmingScore;

    @NotNull(message = "Mathematics score is required")
    @DecimalMin(value = "0.0", message = "Mathematics score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Mathematics score must be between 0 and 100")
    private Double mathematicsScore;

    @NotNull(message = "Technical skills list is required (it can be empty)")
    private List<String> technicalSkills;

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
}
