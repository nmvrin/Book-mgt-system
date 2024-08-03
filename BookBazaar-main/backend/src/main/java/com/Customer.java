package com;

public class Customer extends User {
    private String name;
    private String address;
    private String phone;

    public Customer() {}

    public Customer(String username, String password, String email, String name, String address, String phone) {
        super(username, password, email, "customer");
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
