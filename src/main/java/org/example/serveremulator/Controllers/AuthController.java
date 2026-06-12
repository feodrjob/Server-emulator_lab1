package org.example.serveremulator.Controllers;

import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.AuthRequest;
import org.example.serveremulator.DTO.AuthResponse;
import org.example.serveremulator.Entityes.User;
import org.example.serveremulator.Repositories.UserRepository;
import org.example.serveremulator.Security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    // AuthenticationManager  проверяльщик паролей.
    // вкинули логин и пароль, далее он сам из бд достает тчо нуадо
    private final AuthenticationManager authenticationManager;

    //генерировать токен
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    //шифровальщик паролей
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login") // Сработает при POST-запросе на /api/auth/login
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {


        //спринг проверяет логин и пароль, если пароль не подешел  - ошибка
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        //полносью достали юзера чтобы узнать роль
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        //в новый токен кладем имя юзера и пароль
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(new ApiResponse<>(true, new AuthResponse(token)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) {

        // нет ли уже человека с таким логином в базе данных
        if (userRepository.existsByUsername(user.getUsername())) {
            // Если есть, отдаем ошибку 400 Bad Request
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Пользователь уже существует"));
        }

        //зашифровали
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Пользователь зарегистрирован"));
    }
}