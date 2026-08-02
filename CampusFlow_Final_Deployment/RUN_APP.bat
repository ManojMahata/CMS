@echo off
cd /d "%~dp0"
java -cp "app;lib\mysqlconnector.jar" com.campusflow.ui.LoginWindow
pause
