package com.maryamkhadijah.mk;

public class Employee {
    private String name;
    private String email;
    private String department;
    private String imageUrl;

    // Default constructor required for Firebase database operations
    public Employee() {
    }


    public Employee(String name, String email, String department, String imageUrl) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
