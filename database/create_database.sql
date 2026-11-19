-- Run this in MySQL Workbench if you want to create the database by hand.
-- (The application also creates everything automatically on startup.)
-- Needs MySQL 8.0.16 or newer for the CHECK constraints to be enforced.

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    student_id        INT          PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    email             VARCHAR(150) NOT NULL UNIQUE,
    course            VARCHAR(100) NOT NULL,
    programming_score DECIMAL(5,2) NOT NULL,
    math_score        DECIMAL(5,2) NOT NULL,
    CONSTRAINT chk_programming CHECK (programming_score BETWEEN 0 AND 100),
    CONSTRAINT chk_math        CHECK (math_score BETWEEN 0 AND 100)
);

CREATE TABLE IF NOT EXISTS student_skills (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT         NOT NULL,
    skill      VARCHAR(60) NOT NULL,
    CONSTRAINT fk_skill_student FOREIGN KEY (student_id)
        REFERENCES students (student_id) ON DELETE CASCADE,
    UNIQUE KEY uq_student_skill (student_id, skill)
);

-- Handy checks for screenshots
-- SELECT * FROM students;
-- SELECT * FROM student_skills;
