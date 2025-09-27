package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByName(String name);
    boolean existsById(Long id);

}
