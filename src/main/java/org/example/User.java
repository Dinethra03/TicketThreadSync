package org.example;

public class User {
    private String username;
    private String password;
    private String userType; // Can be "Customer" or "Vendor"

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}

