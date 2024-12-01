package edu.nyu.welcomehome.models;

public enum RoleType {
    STAFF_MEMBER("staff_member"),
    DONOR_MEMBER("donor_member"),
    SUPERVISOR_ROLE("supervisor_role"),
    DELIVERY_AGENT("delivery_agent"),
    CUSTOMER_USER("customer_user");

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