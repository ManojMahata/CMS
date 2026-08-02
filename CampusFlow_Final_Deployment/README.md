# CampusFlow v1.0 - College Management System

**Production-ready desktop application for college administration**

## Quick Start

### Windows
1. Double-click: `RUN_APP.bat`

### Linux/Mac
1. Open terminal in this folder
2. Run: `./run_app.sh`

## First Time Setup

1. **Create Database** (one-time only)
   - Windows: `mysql -u root -p < database\create_database.sql`
   - Linux/Mac: `mysql -u root -p < database/create_database.sql`
   - Enter MySQL root password when prompted

2. **Start Application**
   - Windows: Double-click `RUN_APP.bat`
   - Linux/Mac: Run `./run_app.sh`

3. **Login**
   - Username: `admin`
   - Password: `admin123`
   - **IMPORTANT: Change password immediately!**

## System Requirements

- Java 11 or higher
- MySQL 5.7 or higher
- 4GB RAM minimum
- 500MB disk space
- Windows/Linux/Mac

## Features

**Teacher Module:**
- Mark attendance (real-time percentage)
- Enter marks with grades
- View reports (attendance, marks, students)
- Upload question papers

**Admin Module:**
- Manage students
- Manage teachers
- Manage subjects
- Track fees
- View system statistics

## Troubleshooting

**Error: "Cannot connect to database"**
- Check MySQL is running
- Verify database created: `mysql -u root -p -e "USE campusflow_db; SHOW TABLES;"`

**Error: "Class not found"**
- Verify `lib/mysqlconnector.jar` exists
- Check Java is installed: `java -version`

**Error: "Port already in use"**
- Another application using port 3306
- Change MySQL port or restart MySQL

## Default Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher (Test) | ram.sharma | manoj123 |

## Database Backup

```bash
mysqldump -u root -p campusflow_db > backup.sql
```

## Support

For issues:
1. Check terminal output
2. Verify MySQL is running
3. Check Java version
4. Review this README

---

**Developed for Tribhuvan University, Nepal**  
**Version 1.0 | July 2026**
