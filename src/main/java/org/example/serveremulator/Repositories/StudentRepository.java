package org.example.serveremulator.Repositories;



import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByGroupId(Long groupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.group.id = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
}