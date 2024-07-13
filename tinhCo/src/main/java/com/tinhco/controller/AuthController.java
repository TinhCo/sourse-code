package com.tinhco.controller;

import com.tinhco.auth.entities.RefreshToken;
import com.tinhco.auth.entities.User;
import com.tinhco.auth.sevices.AuthService;
import com.tinhco.auth.sevices.JwtService;
import com.tinhco.auth.sevices.RefreshTokenService;
import com.tinhco.auth.utils.AuthResponse;
import com.tinhco.auth.utils.LoginRequest;
import com.tinhco.auth.utils.RefreshTokenRequest;
import com.tinhco.auth.utils.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    // endpoint to authenticate user for login
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        String token = this.jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .token(token)
                .build());
    }
}
