package org.example.serveremulator.Security;

import org.example.serveremulator.Entityes.User;
import org.example.serveremulator.Repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    //вызывается каждый раз когда кто-то пытается залогиниться
    private final UserRepository userRepository;
    //ищем юзера в бд
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //юзер -> юзер в спринге
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                //выдаем роль
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

}
