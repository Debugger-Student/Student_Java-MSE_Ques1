package com.student.management.service;

import com.student.management.dto.MeritListEntry;
import com.student.management.dto.StudentResponse;
import com.student.management.dto.StudentUpdateRequest;
import com.student.management.exception.DuplicateStudentException;
import com.student.management.exception.StudentNotFoundException;
import com.student.management.model.Student;
import com.student.management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/** Business rules live here: average, category, stronger subject, merit list, skill groups. */
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Merit order: higher average first, then higher programming score, then name (A-Z)
    private static final Comparator<Student> MERIT_ORDER = (a, b) -> {
        int result = Double.compare(average(b), average(a));
        if (result != 0) {
            return result;
        }
        result = Double.compare(b.getProgrammingScore(), a.getProgrammingScore());
        if (result != 0) {
            return result;
        }
        return a.getName().compareToIgnoreCase(b.getName());
    };

    // ---------------------------------------------------------------- CRUD

    @Transactional
    public StudentResponse addStudent(Student student) {
        int id = student.getStudentId();
        if (repository.existsById(id)) {
            throw new DuplicateStudentException("Student ID " + id + " already exists");
        }
        if (repository.existsByEmail(student.getEmail())) {
            throw new DuplicateStudentException("Email " + student.getEmail() + " is already registered");
        }
        repository.save(student);
        return getStudentById(id);
    }

    public List<StudentResponse> getAllStudents() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(int id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return toResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(int id, StudentUpdateRequest request) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.updateScoresAndSkills(id, request.getProgrammingScore(),
                request.getMathematicsScore(), request.getTechnicalSkills());
        return getStudentById(id);
    }

    @Transactional
    public void deleteStudent(int id) {
        if (!repository.deleteById(id)) {
            throw new StudentNotFoundException(id);
        }
    }

    // ------------------------------------------------------------ Merit list

    public List<MeritListEntry> getMeritList() {
        List<Student> students = new ArrayList<>(repository.findAll());
        students.sort(MERIT_ORDER);

        List<MeritListEntry> meritList = new ArrayList<>();
        int rank = 1;
        for (Student s : students) {
            double avg = average(s);
            meritList.add(new MeritListEntry(rank++, s.getStudentId(), s.getName(),
                    s.getCourseName(), avg, category(avg)));
        }
        return meritList;
    }

    // ------------------------------------------------- Extra business rules

    /** Search by email or course (search by ID is GET /api/students/{id}). */
    public List<StudentResponse> search(String email, String course) {
        List<Student> found;
        if (email != null && !email.isBlank()) {
            found = repository.findByEmail(email.trim());
        } else if (course != null && !course.isBlank()) {
            found = repository.findByCourse(course.trim());
        } else {
            throw new IllegalArgumentException(
                    "Provide 'email' or 'course' to search (to search by ID use /api/students/{id})");
        }
        return found.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Students with the same technical skills, in two ways:
     * 1) for each skill, the students who have it (Map: skill -> students)
     * 2) students whose complete skill set is identical (Map: skill set -> students)
     */
    public Map<String, Object> getSameSkillGroups() {
        Map<String, List<String>> bySkill = new TreeMap<>();
        Map<String, List<String>> bySkillSet = new TreeMap<>();

        for (Student s : repository.findAll()) {
            String label = s.getStudentId() + " - " + s.getName();

            Set<String> skillSet = new TreeSet<>();          // Set: removes duplicates, sorted
            for (String skill : s.getTechnicalSkills()) {
                skillSet.add(skill.toLowerCase());
            }
            for (String skill : skillSet) {
                bySkill.computeIfAbsent(skill, key -> new ArrayList<>()).add(label);
            }
            if (!skillSet.isEmpty()) {
                bySkillSet.computeIfAbsent(String.join(", ", skillSet), key -> new ArrayList<>()).add(label);
            }
        }
        // keep only groups that have at least two students
        bySkill.values().removeIf(list -> list.size() < 2);
        bySkillSet.values().removeIf(list -> list.size() < 2);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentsSharingEachSkill", bySkill);
        result.put("studentsWithIdenticalSkillSets", bySkillSet);
        return result;
    }

    /** Programming needs improvement (below 50) but mathematics is at least 75. */
    public List<StudentResponse> getNeedsProgrammingSupport() {
        return repository.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------- Helpers

    // Average of the two scores, rounded to 2 decimals
    static double average(Student s) {
        double avg = (s.getProgrammingScore() + s.getMathematicsScore()) / 2.0;
        return Math.round(avg * 100.0) / 100.0;
    }

    static String category(double average) {
        if (average >= 85) {
            return "Excellent";
        } else if (average >= 70) {
            return "Good";
        } else if (average >= 50) {
            return "Satisfactory";
        }
        return "Needs Improvement";
    }

    static String strongerSubject(Student s) {
        int comparison = Double.compare(s.getProgrammingScore(), s.getMathematicsScore());
        if (comparison > 0) {
            return "Programming";
        } else if (comparison < 0) {
            return "Mathematics";
        }
        return "Both Equal";
    }

    private StudentResponse toResponse(Student s) {
        double avg = average(s);
        return new StudentResponse(s.getStudentId(), s.getName(), s.getEmail(), s.getCourseName(),
                s.getProgrammingScore(), s.getMathematicsScore(), s.getTechnicalSkills(),
                avg, category(avg), strongerSubject(s));
    }
}
