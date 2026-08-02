package com.campusflow.utils;

public class GetHash {
    public static void main(String[] args) {
        if (args.length > 0) {
            String password = args[0];
            String hash = PasswordHasher.hashPassword(password);
            System.out.println("Password: " + password);
            System.out.println("Hash:     " + hash);
        } else {
            System.out.println("Usage: java GetHash <password>");
        }
    }
}