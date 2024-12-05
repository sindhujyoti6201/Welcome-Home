package edu.nyu.welcomehome.models;

public enum RoleType {
    STAFF("STAFF"),
    DONOR("DONOR"),
    DELIVERY_AGENT("DELIVERY AGENT"),
    BORROWER("BORROWER"),
    MANAGER("MANAGER"),
    SUPERVISOR("SUPERVISOR");

    private final String role;

    // Constructor
    RoleType(String role) {
        this.role = role;
    }

    // Getter method
    public String getRole() {
        return role;
    }
}