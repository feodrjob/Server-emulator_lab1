package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByLessonId(Long lessonId);
    boolean existsByLessonId(Long lessonId);

    @Modifying
    void deleteByLessonId(Long lessonId);
}