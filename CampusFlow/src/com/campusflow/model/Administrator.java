package com.campusflow.model;

/**
 * administrator- represent an admin user
 */

public class Administrator {

    // attributes and enpasulation
    private String adminId;
    private String name;
    private String email;
    private String username;
    private String passwordHash;

    // constructor
    public Administrator(String adminId, String name, String email, String username, String passwordHash) {

        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // getters
    public String getAdminId() {return adminId;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getUsername() {return username;}
    public String getPasswordHash() {return passwordHash;}

    // setters
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId='" + adminId + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
     }
    
}// main class ends here