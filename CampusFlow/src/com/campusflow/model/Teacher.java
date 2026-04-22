package com.campusflow.model;

public class Teacher {

        // encapuslation
        private String teacherId;
        private String name;
        private String department;
        private String email;
        private String phone;
        private String username;
        private String passwordHash;

        public  Teacher(String teacherId, String name, String department, String email, String phone, String username, String passwordHash){
            this.teacherId = teacherId;
            this.name = name;
            this.department = department;
            this.email = email;
            this.phone = phone;
            this.username = username;
            this.passwordHash = passwordHash;
        }

        
        // getters starts from here

        public String getTeacherId(){
            return teacherId;
        }

        public String getName(){
            return name;
        }

        public String getDepartment(){
            return department;
        }

        public String getEmail(){
            return email;
        }

        public String getPhone(){
            return phone;
        }

        public String getUsername(){
            return username;
        }

        public String getPasswordHash(){
            return passwordHash;
        }

        // getter finished
        // setter starts

        public void setName(String name){
            this.name = name;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setEmail(String email){
            this.email = email;
        }

        public void setPhone(String phone){
            this.phone = phone;
        }

        // setter finished here
        // for userName and passwordHash we are not using setters those two are set once.

        @Override
        public String toString(){
            return "Teacher{" + 
                    "Id='" + teacherId + '\'' +
                    "Name='" + name + '\'' + 
                    "Department='" + department + '\'' + 
                    "Email='" + email + '\'' +
                    "Phone='" + phone + '\'' +
                    "UserName='" + username + '\'' +
                    '}';
        }// to string method closed



   
}// main class ends here