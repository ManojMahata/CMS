package com.campusflow.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*PasswordHasher - Utility class for secure password hashing,
Using SHA-256 algorithm 
*/

public class PasswordHasher {

    // admin sets password
    // class returns hashed password 
    
    public static String hashPassword(String password){
        try{
            // create sha-256 hash instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert password to bytes and hash it
            byte[] hashedBytes = digest.digest(password.getBytes());

            // convert bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();

            for ( byte b : hashedBytes){
                String hex = Integer.toHexString(0xff & b);

                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found!");
            e.printStackTrace();
            return null;
        }
 
    }// hashPassword method ends here

    /*
    Verify if entered password matches stored hash
    enterPassword 
    storedHash hash from database
    return if mathches
    */

    public static boolean verifyPassword(String enteredPassword, String storedHash){
        
        String enteredHash = hashPassword(enteredPassword);

        return enteredHash != null && enteredHash.equals(storedHash);

    }

    /* Tesst teh password hasher */

    public static void main(String[] args){
        System.out.println("=== Password Hasher Test ===\n");

        // test 1: hash a password
        String passwrod1 = "mypassword123";
        String hash1 = hashPassword(passwrod1);
        System.out.println("Password: " + passwrod1);
        System.out.println("Hash: " + hash1);
        System.out.println();

        // test 2: same password alwats produces same hash 
        String hash2 = hashPassword(passwrod1);
        System.out.println("Hash again: " + hash2);
        System.out.println("Hash mathch? " + hash1.equals(hash2));
        System.out.println();

        // test 3: different password produces dirrerent hash

        String password2 = "mypassword124";
        String hash3 = hashPassword(password2);
        System.out.println("Different password: " + password2);
        System.out.println("Hash of password2: " + hash3);
        System.out.println("Differnet from first password? " + !hash1.equals(hash3));

        // test4: verify password
        System.out.println("=== Password Verification ===");
        System.out.println("Correct password: " + verifyPassword("mypassword123", hash1));
        System.out.println("Wrong password: " + verifyPassword( "wrongpassword", hash1));

    }// password testing method ends here


}// main class ends here