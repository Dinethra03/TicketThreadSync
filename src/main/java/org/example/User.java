package org.example;

public class User {
    private String username;
    private String password;
    private String userType; // Declare a private field to store the user's type (customer or vendor)

    //Constructor to create a new user object with the specified username, password and user type
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }


    //Override the toString method and provide a string representation of the user object
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}

