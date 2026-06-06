package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //Проверка занят ли логин
    boolean existsByUsername(String username);
}
