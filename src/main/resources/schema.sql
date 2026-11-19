-- Tables are created automatically when the application starts.

CREATE TABLE IF NOT EXISTS students (
    student_id        INT          PRIMARY KEY,               -- duplicate IDs are rejected by MySQL
    name              VARCHAR(100) NOT NULL,
    email             VARCHAR(150) NOT NULL UNIQUE,
    course            VARCHAR(100) NOT NULL,
    programming_score DECIMAL(5,2) NOT NULL,
    math_score        DECIMAL(5,2) NOT NULL,
    CONSTRAINT chk_programming CHECK (programming_score BETWEEN 0 AND 100),
    CONSTRAINT chk_math        CHECK (math_score BETWEEN 0 AND 100)
);

-- One row per skill, so a student's List<String> of skills is stored properly
CREATE TABLE IF NOT EXISTS student_skills (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT         NOT NULL,
    skill      VARCHAR(60) NOT NULL,
    CONSTRAINT fk_skill_student FOREIGN KEY (student_id)
        REFERENCES students (student_id) ON DELETE CASCADE,
    UNIQUE KEY uq_student_skill (student_id, skill)
);
