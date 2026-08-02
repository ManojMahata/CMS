#!/bin/bash
cd "$(dirname "$0")"
java -cp "app:lib/mysqlconnector.jar" com.campusflow.ui.LoginWindow
