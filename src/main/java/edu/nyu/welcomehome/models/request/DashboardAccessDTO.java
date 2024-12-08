package edu.nyu.welcomehome.models.request;

import java.util.List;

public class DashboardAccessDTO {
    private final String username;
    private final List<String> roles;

    public DashboardAccessDTO(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}