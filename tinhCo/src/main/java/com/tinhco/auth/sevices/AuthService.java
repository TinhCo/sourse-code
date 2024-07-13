package com.tinhco.auth.sevices;

import com.tinhco.auth.entities.User;
import com.tinhco.auth.entities.UserRole;
import com.tinhco.auth.repositories.UserRepository;
import com.tinhco.auth.utils.AuthResponse;
import com.tinhco.auth.utils.LoginRequest;
import com.tinhco.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    /*
     * ĐĂNG KÝ NGƯỜI DÙNG MỚI
     * xây dựng đối tượng người dùng bằng đối tượng yêu cầu
     * lưu người dùng trong DB
     * tạo JWT/refreshToken và gửi phản hồi
     */
    public AuthResponse register(RegisterRequest request){
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();
        User savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getRefreshToken())
                .role(savedUser.getRole().name())
                .build();

    }
    /*
     * NGƯỜI DÙNG ĐĂNG NHẬP
     * sử dụng AuthenticationManager để xác thực người dùng
     * lấy thông tin người dùng
     * tạo JWT/refreshToken và gửi phản hồi
     */
    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getRefreshToken())
                .role(user.getRole().name())
                .build();
    }

}
