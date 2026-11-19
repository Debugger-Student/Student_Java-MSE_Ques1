package com.student.management.repository;

import com.student.management.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** All SQL lives here. Uses JdbcTemplate, so every query is a PreparedStatement. */
@Repository
public class StudentRepository {

    private final JdbcTemplate jdbc;

    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Turns one database row into a Student object (skills are attached afterwards)
    private final RowMapper<Student> studentMapper = (rs, rowNum) -> {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setCourseName(rs.getString("course"));
        s.setProgrammingScore(rs.getDouble("programming_score"));
        s.setMathematicsScore(rs.getDouble("math_score"));
        return s;
    };

    public boolean existsById(int id) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM students WHERE student_id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM students WHERE LOWER(email) = LOWER(?)", Integer.class, email);
        return count != null && count > 0;
    }

    public void save(Student s) {
        jdbc.update("INSERT INTO students (student_id, name, email, course, programming_score, math_score) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                s.getStudentId(), s.getName(), s.getEmail(), s.getCourseName(),
                s.getProgrammingScore(), s.getMathematicsScore());
        saveSkills(s.getStudentId(), s.getTechnicalSkills());
    }

    public void updateScoresAndSkills(int id, double programming, double maths, List<String> skills) {
        jdbc.update("UPDATE students SET programming_score = ?, math_score = ? WHERE student_id = ?",
                programming, maths, id);
        jdbc.update("DELETE FROM student_skills WHERE student_id = ?", id);
        saveSkills(id, skills);
    }

    public boolean deleteById(int id) {
        // student_skills rows are removed automatically (ON DELETE CASCADE)
        return jdbc.update("DELETE FROM students WHERE student_id = ?", id) > 0;
    }

    public Optional<Student> findById(int id) {
        List<Student> list = jdbc.query("SELECT * FROM students WHERE student_id = ?", studentMapper, id);
        attachSkills(list);
        return list.stream().findFirst();
    }

    public List<Student> findAll() {
        List<Student> list = jdbc.query("SELECT * FROM students ORDER BY student_id", studentMapper);
        attachSkills(list);
        return list;
    }

    public List<Student> findByEmail(String email) {
        List<Student> list = jdbc.query(
                "SELECT * FROM students WHERE LOWER(email) = LOWER(?)", studentMapper, email);
        attachSkills(list);
        return list;
    }

    public List<Student> findByCourse(String course) {
        List<Student> list = jdbc.query(
                "SELECT * FROM students WHERE LOWER(course) = LOWER(?) ORDER BY student_id",
                studentMapper, course);
        attachSkills(list);
        return list;
    }

    private void saveSkills(int studentId, List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return;
        }
        // LinkedHashMap keeps the original order and drops duplicates (Java, java, JAVA -> one skill)
        Map<String, String> unique = new LinkedHashMap<>();
        for (String skill : skills) {
            if (skill != null && !skill.trim().isEmpty()) {
                unique.putIfAbsent(skill.trim().toLowerCase(), skill.trim());
            }
        }
        for (String skill : unique.values()) {
            jdbc.update("INSERT INTO student_skills (student_id, skill) VALUES (?, ?)", studentId, skill);
        }
    }

    // One query for all skills, then grouped in a Map<studentId, List<skill>> (no N+1 queries)
    private void attachSkills(List<Student> students) {
        if (students.isEmpty()) {
            return;
        }
        Map<Integer, List<String>> skillsByStudent = new HashMap<>();
        jdbc.query("SELECT student_id, skill FROM student_skills ORDER BY id",
                (RowCallbackHandler) rs -> skillsByStudent
                        .computeIfAbsent(rs.getInt("student_id"), key -> new ArrayList<>())
                        .add(rs.getString("skill")));
        for (Student s : students) {
            s.setTechnicalSkills(skillsByStudent.getOrDefault(s.getStudentId(), new ArrayList<>()));
        }
    }
}
