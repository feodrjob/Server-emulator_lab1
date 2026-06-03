package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Attendance;
import org.example.serveremulator.Entityes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByLessonId(Long lessonId);
    boolean existsByLessonId(Long lessonId);

    @Modifying
    @Query("DELETE FROM Attendance a WHERE :student MEMBER OF a.presentStudents")
    void deleteStudentFromAllAttendances(@Param("student") Student student);
    //Если студент посетил хотя бы 1 раз, значит он привязан к итории занятий
    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE :student MEMBER OF  a.presentStudents")
    boolean isStudentPresentInAnyAttendance(@Param("student") Student student);
}