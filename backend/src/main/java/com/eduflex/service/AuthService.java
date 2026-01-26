package com.eduflex.service;

import com.eduflex.dto.request.LoginRequest;
import com.eduflex.dto.request.RegisterRequest;
import com.eduflex.dto.response.AuthResponse;
import com.eduflex.exception.BadRequestException;
import com.eduflex.model.entity.User;
import com.eduflex.model.enums.UserRole;
import com.eduflex.model.enums.UserStatus;
import com.eduflex.repository.UserRepository;
import com.eduflex.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

        @Transactional
        public AuthResponse register(RegisterRequest request) {
                // Check if user already exists
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new BadRequestException("Email already registered");
                }

                // Determine user status based on role
                UserStatus status = request.getRole() == UserRole.INSTRUCTOR
                                ? UserStatus.PENDING_APPROVAL
                                : UserStatus.ACTIVE;

                // Create new user
                User user = User.builder()
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .role(request.getRole())
                                .status(status)
                                .bio(request.getBio())
                                .build();

                user = userRepository.save(user);

                // Generate JWT token
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

                return AuthResponse.builder()
                                .token(token)
                                .type("Bearer")
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole().name())
                                .status(user.getStatus().name())
                                .build();
        }

        public AuthResponse login(LoginRequest request) {
                // Authenticate user
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                // Get user details
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BadRequestException("User not found"));

                // Check if user is active
                if (user.getStatus() != UserStatus.ACTIVE) {
                        throw new BadRequestException("Account is not active. Please wait for approval.");
                }

                // Generate JWT token
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

                return AuthResponse.builder()
                                .token(token)
                                .type("Bearer")
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .role(user.getRole().name())
                                .status(user.getStatus().name())
                                .build();
        }
}
