package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);

    List<Lesson> findByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate);

    @Modifying
    void deleteByTeacherId(Long teacherId);

    @Modifying
    void deleteByGroupId(Long groupId);
}