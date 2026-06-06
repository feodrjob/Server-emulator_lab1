package org.example.serveremulator.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//перехватываем http запросы приходящие на сервер, смотрим заголвоки
//берем токен и расшифровываем его
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        //достаем заголовок Authorization
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        //проверяем есть ли заголовок и начинается ли он с Bearer
        if  (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);// отрезали Bearer
            try{
                username = jwtUtil.extractUsername(jwt); //достаем логи из токена
            }catch (Exception e){
                logger.error( " Не удалось извлечь JWT токен"+ e.getMessage());
            }
        }
        //если достали логин, а в контексте безопасности спринги пока пусто
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //загружаем юзера из базы
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //Проверим токен на валидность
            if(jwtUtil.validateToken(jwt, userDetails.getUsername())){
                //если все нормально создаем пропуск для спринги
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //скалыдываем пропуск в контекст безопасности (теперь сприга знает кто делает запрос)
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        //передаем запрос дальше по цепочке
        chain.doFilter(request, response);
    }
}
