package org.example.serveremulator.Repositories;


import org.example.serveremulator.Entityes.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    boolean existsByName(String lastName, String firstName, String middleName);
}
