-- Database: campusflow_db
-- SDLC Phase 2: System Design
-- Created by: Manoj

-- Create database
CREATE DATABASE IF NOT EXISTS campusflow_db;
USE campusflow_db;

-- Table 1: Students
CREATE TABLE students (
    roll_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    faculty VARCHAR(50) NOT NULL,
    course VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    section VARCHAR(10),
    email VARCHAR(100),
    phone VARCHAR(15),
    fee_status ENUM('Pending', 'Paid') DEFAULT 'Pending',
    date_of_admission DATE NOT NULL
);

-- Table 2: Teachers
CREATE TABLE teachers (
    teacher_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,  -- SHA-256 hash
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 3: Administrators
CREATE TABLE administrators (
    admin_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 4: Subjects
CREATE TABLE subjects (
    subject_code VARCHAR(20) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    course VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    assigned_teacher VARCHAR(20),
    FOREIGN KEY (assigned_teacher) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

-- Table 5: Attendance Records
CREATE TABLE attendance_records (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,  -- Denormalized for performance
    subject_code VARCHAR(20) NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    marked_by VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code) ON DELETE CASCADE,
    FOREIGN KEY (marked_by) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_roll, subject_code, attendance_date)
);

-- Table 6: Marks/Grades
CREATE TABLE marks (
    marks_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    subject_code VARCHAR(20) NOT NULL,
    exam_type ENUM('Internal', 'Final', 'Assignment') NOT NULL,
    marks_obtained DECIMAL(5,2) NOT NULL,
    total_marks DECIMAL(5,2) NOT NULL,
    entered_by VARCHAR(20) NOT NULL,
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code) ON DELETE CASCADE,
    FOREIGN KEY (entered_by) REFERENCES teachers(teacher_id) ON DELETE CASCADE
);

-- Table 7: Fee Records
CREATE TABLE fee_records (
    fee_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    total_fee DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) DEFAULT 0,
    pending_amount DECIMAL(10,2) NOT NULL,
    last_payment_date DATE,
    updated_by VARCHAR(20) NOT NULL,
    update_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_roll) REFERENCES students(roll_number) ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES administrators(admin_id) ON DELETE CASCADE
);

-- Table 8: Question Papers
CREATE TABLE question_papers (
    paper_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL,
    exam_type ENUM('Internal', 'Final', 'Assignment') NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    semester INT NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(20) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES teachers(teacher_id) ON DELETE CASCADE
);

-- Table 9: Audit Log (for admin actions)
CREATE TABLE audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id VARCHAR(20) NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    description TEXT,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES administrators(admin_id) ON DELETE CASCADE
);