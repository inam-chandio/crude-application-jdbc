package com.store.controller;

import com.store.model.RoomApplication;  // Updated import
import com.store.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyForRoom(
            @RequestParam Long userId,
            @RequestParam Long roomId) {
        try {
            RoomApplication application = applicationService.applyForRoom(userId, roomId);
            if (application != null) {
                return ResponseEntity.ok(application);
            }
            return ResponseEntity.badRequest().body("Invalid user or room ID");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{applicationId}/cancel")
    public ResponseEntity<?> cancelApplication(
            @PathVariable Long applicationId,
            @RequestParam Long userId) {
        RoomApplication cancelled = applicationService.cancelApplication(applicationId, userId);
        if (cancelled != null) {
            return ResponseEntity.ok(cancelled);
        }
        return ResponseEntity.badRequest().body("Cannot cancel application");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomApplication>> getUserApplications(@PathVariable Long userId) {
        List<RoomApplication> applications = applicationService.findAllApplicationsByUserId(userId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<RoomApplication>> getUserApplicationsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        List<RoomApplication> applications = applicationService.findApplicationsByStatus(status, userId);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody Map<String, String> status) {
        RoomApplication updated = applicationService.updateApplicationStatus(applicationId, status.get("status"));
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.badRequest().body("Cannot update application status");
    }
}