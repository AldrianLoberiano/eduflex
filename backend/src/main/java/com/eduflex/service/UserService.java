package com.eduflex.service;

import com.eduflex.dto.response.UserResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.exception.ResourceNotFoundException;
import com.eduflex.model.entity.User;
import com.eduflex.model.enums.UserRole;
import com.eduflex.model.enums.UserStatus;
import com.eduflex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getPendingInstructors() {
        return userRepository.findByRoleAndStatus(UserRole.INSTRUCTOR, UserStatus.PENDING_APPROVAL)
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse approveInstructor(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        if (instructor.getRole() != UserRole.INSTRUCTOR) {
            throw new BadRequestException("User is not an instructor");
        }

        instructor.setStatus(UserStatus.ACTIVE);
        instructor = userRepository.save(instructor);
        return mapToUserResponse(instructor);
    }

    @Transactional
    public UserResponse updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(status);
        user = userRepository.save(user);
        return mapToUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
