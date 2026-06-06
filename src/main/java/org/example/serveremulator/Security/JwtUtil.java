package org.example.serveremulator.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.security.Key;

@Component
public class JwtUtil {
    //секретный ключ для цифровой подписи токена
    //хранится в памяти сервера -> не можем нарисовать токен админа
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //токен действует 1 час
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60;

    // методы чтения токена

    //вытаскиваем имя пользователя из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Расшифровываем токен с помощью нашего секретного ключа
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    // Проверка: не истек ли срок годности токена?
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //генерация токена
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        //складываем роль в токен чтобы знать какие права есть
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                                           // роль
                .setSubject(subject)                                         // кому выдан (логин)
                .setIssuedAt(new Date(System.currentTimeMillis()))           // когда выдан
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY)) // до какого годен
                .signWith(SECRET_KEY)                                        // ставим печать
                .compact();
    }

    // токен принадлежит этому юзеру и не просрочен ли он?
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
