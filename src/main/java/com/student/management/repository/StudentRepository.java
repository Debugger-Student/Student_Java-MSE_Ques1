package com.student.management.repository;

import com.student.management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT DISTINCT s FROM Student s JOIN s.technicalSkills skill " +
           "WHERE LOWER(skill) = LOWER(:skill)")
    List<Student> searchByTechnicalSkill(@Param("skill") String skill);

    List<Student> findByCourseNameIgnoreCase(String courseName);
}
