package edu.nyu.welcomehome.daos;

import java.util.List;

public record DashboardAccessDTO(String username, List<String> roles) {
}