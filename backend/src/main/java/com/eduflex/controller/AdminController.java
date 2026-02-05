package com.eduflex.controller;

import com.eduflex.dto.response.ApiResponse;
import com.eduflex.dto.response.EnrollmentResponse;
import com.eduflex.dto.response.UserResponse;
import com.eduflex.model.enums.UserRole;
import com.eduflex.model.enums.UserStatus;
import com.eduflex.service.EnrollmentService;
import com.eduflex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/instructors/pending")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getPendingInstructors() {
        List<UserResponse> instructors = userService.getPendingInstructors();
        return ResponseEntity.ok(ApiResponse.success(instructors));
    }

    @PutMapping("/instructors/{instructorId}/approve")
    public ResponseEntity<ApiResponse<UserResponse>> approveInstructor(@PathVariable Long instructorId) {
        UserResponse user = userService.approveInstructor(instructorId);
        return ResponseEntity.ok(ApiResponse.success("Instructor approved successfully", user));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam UserStatus status) {
        UserResponse user = userService.updateUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success("User status updated", user));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAllEnrollments() {
        List<EnrollmentResponse> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
}
