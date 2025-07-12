package com.bookingsystem.infrastructure.adapter.web;

import com.bookingsystem.application.service.AuthenticationService;
import com.bookingsystem.domain.model.User;
import com.bookingsystem.infrastructure.adapter.web.dto.LoginRequest;
import com.bookingsystem.infrastructure.adapter.web.dto.LoginResponse;
import com.bookingsystem.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    
    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = authenticationService.authenticate(request.getUsername(), request.getPassword());
        
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(user.get());
            LoginResponse response = new LoginResponse(
                token,
                user.get().getUsername(),
                user.get().getRole().name()
            );
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).build();
    }
}