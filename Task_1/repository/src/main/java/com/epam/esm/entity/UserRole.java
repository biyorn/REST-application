package com.epam.esm.entity;

public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private String title;

    public String getTitle() {
        return title;
    }

    UserRole(String title) {
        this.title = title;
    }
}
