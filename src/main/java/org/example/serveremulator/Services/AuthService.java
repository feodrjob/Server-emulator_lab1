package org.example.serveremulator.Services;

import org.example.serveremulator.Entityes.User;
import org.example.serveremulator.Enums.Role;
import org.example.serveremulator.Repositories.UserRepository;
import org.example.serveremulator.Security.CustomUserDetailsService;
import org.example.serveremulator.Security.JwtUtil;
import org.example.serveremulator.DTO.AuthRequest;
import org.example.serveremulator.DTO.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // регистрация
    public AuthResponse register(AuthRequest request) {
        // проверка на существование
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("пользователь с таким именем уже существует!");
        }

        // создание юзера
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // если роль не передали, по умолчанию ставим студента
        if (request.getRole() == null || request.getRole().isEmpty()) {
            newUser.setRole(Role.ROLE_STUDENT);
        } else {
            // если передали (например, "ROLE_ADMIN"), то ставим её
            newUser.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }

        userRepository.save(newUser);

        // генерация токена
        String token = jwtUtil.generateToken(newUser.getUsername(), newUser.getRole().name());

        // возвращаем только токен
        return new AuthResponse(token);
    }

    // логин
    public AuthResponse login(AuthRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // сверка паролей
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("неверный пароль!");
        }

        // получение роли из данных спринга
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        // возвращаем только токен
        return new AuthResponse(token);
    }
}