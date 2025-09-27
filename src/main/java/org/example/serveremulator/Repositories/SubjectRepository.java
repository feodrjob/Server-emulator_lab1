package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    boolean existsByName(String name);

}
