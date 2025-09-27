package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Attendance;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//Посещаемость

@Repository
public interface AttendanceRepository {
    Optional<Attendance> findByLessonId(Long lessonId);

    void deleteByLessonId(Long lessonId);
}
