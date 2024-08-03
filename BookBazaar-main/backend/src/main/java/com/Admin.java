package com;

public class Admin extends User {
    private String role;

    public Admin() {}

    public Admin(String username, String password, String email, String role) {
        super(username, password, email, role);
        this.role = role;
    }

    // Getters and setters
    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
