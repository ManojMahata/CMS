-- CampusFlow Database Setup
DROP DATABASE IF EXISTS campusflow_db;
CREATE DATABASE campusflow_db;
USE campusflow_db;

CREATE TABLE students (
    roll_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    faculty VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    fee_status VARCHAR(20) DEFAULT 'Pending',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teachers (
    teacher_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE administrators (
    admin_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subjects (
    subject_code VARCHAR(20) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    course VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    assigned_teacher VARCHAR(20),
    FOREIGN KEY (assigned_teacher) REFERENCES teachers(teacher_id)
);

CREATE TABLE attendance_records (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(20) NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    marked_by VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_attendance (student_roll, subject_code, attendance_date),
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code),
    FOREIGN KEY (marked_by) REFERENCES teachers(teacher_id)
);

CREATE TABLE marks (
    marks_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    subject_code VARCHAR(20) NOT NULL,
    exam_type VARCHAR(20) NOT NULL,
    marks_obtained DECIMAL(5,2) NOT NULL,
    total_marks DECIMAL(5,2) NOT NULL,
    entered_by VARCHAR(20) NOT NULL,
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code),
    FOREIGN KEY (entered_by) REFERENCES teachers(teacher_id)
);

CREATE TABLE fee_records (
    fee_id INT AUTO_INCREMENT PRIMARY KEY,
    student_roll VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE,
    status VARCHAR(20) DEFAULT 'Pending',
    processed_by VARCHAR(20),
    FOREIGN KEY (student_roll) REFERENCES students(roll_number),
    FOREIGN KEY (processed_by) REFERENCES administrators(admin_id)
);

CREATE TABLE question_papers (
    paper_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL,
    exam_type VARCHAR(20),
    academic_year VARCHAR(20),
    semester INT,
    file_path VARCHAR(255) NOT NULL,
    uploaded_by VARCHAR(20) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code),
    FOREIGN KEY (uploaded_by) REFERENCES teachers(teacher_id)
);

CREATE TABLE audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    performed_by VARCHAR(20),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE USER IF NOT EXISTS 'campusflow_user'@'localhost' IDENTIFIED BY 'CampusFlow@2025';
GRANT ALL PRIVILEGES ON campusflow_db.* TO 'campusflow_user'@'localhost';
FLUSH PRIVILEGES;

INSERT INTO administrators VALUES ('ADM001', 'System Administrator', 'admin@college.edu', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', NOW());

COMMIT;
