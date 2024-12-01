package edu.nyu.welcomehome.controllers;

import edu.nyu.welcomehome.models.request.DashboardAccessDTO;
import edu.nyu.welcomehome.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/dashboard/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDashboardAccess(@PathVariable String username) {
        try {
            DashboardAccessDTO access = dashboardService.getDashboardAccess(username);

            if (!hasAnyAccess(access)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. No valid roles assigned."));
            }

            return ResponseEntity.ok(access);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error occurred: " + e.getMessage()));
        }
    }

    private boolean hasAnyAccess(DashboardAccessDTO access) {
        return access.isHasDeliveryAccess() ||
                access.isHasDonationAccess() ||
                access.isHasSupervisorAccess() ||
                access.isHasManagerAccess();
    }
}
