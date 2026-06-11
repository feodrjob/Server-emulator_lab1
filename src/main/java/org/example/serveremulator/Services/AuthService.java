package org.example.serveremulator.Services;

import org.example.serveremulator.Entityes.User;
import org.example.serveremulator.Enums.Role;
import org.example.serveremulator.Repositories.UserRepository;
import org.example.serveremulator.Security.CustomUserDetailsService;
import org.example.serveremulator.Security.JwtUtil;
import org.example.serveremulator.DTO.AuthRequest;
import org.example.serveremulator.DTO.AuthResponse;
import org.springframework.security.core.GrantedAuthority;
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

    public AuthResponse register(AuthRequest request) {
        // 1. Проверяем, нет ли такого юзера (используем твой существующий findByUsername)
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует!");
        }

        // 2. Создаем и заполняем нового юзера
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        // Шифруем пароль перед сохранением в БД
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Устанавливаем роль (берем строку из запроса, переводим в верхний регистр и превращаем в Enum)
        newUser.setRole(Role.valueOf(request.getRole().toUpperCase()));

        // 4. Сохраняем в базу
        userRepository.save(newUser);

        // 5. Генерируем токен, передавая ИМЯ и РОЛЬ (как требует твой JwtUtil)
        String token = jwtUtil.generateToken(newUser.getUsername(), newUser.getRole().name());

        return new AuthResponse(token, "Регистрация успешно завершена!");
    }

    public AuthResponse login(AuthRequest request) {
        // 1. Загружаем UserDetails (внутри него есть зашифрованный пароль и роли)
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // 2. Сравниваем пароли
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Неверный пароль!");
        }

        // 3. Достаем роль из UserDetails.
        // Так как у нас одна роль, мы просто берем первый элемент из коллекции authorities
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        // 4. Генерируем токен с именем и ролью
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        return new AuthResponse(token, "Вход выполнен успешно!");
    }
}