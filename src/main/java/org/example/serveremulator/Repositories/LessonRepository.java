package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Lesson;

import org.example.serveremulator.Entityes.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @EntityGraph(attributePaths = {"teacher", "subject", "group"})
    @Override
    List<Lesson> findAll();

    @EntityGraph(attributePaths = {"teacher", "subject", "group"})
    List<Lesson> findByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"teacher", "subject", "group"})
    List<Lesson> findByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate);

    boolean existsByGroupIdAndDateAndLessonNumber(Long groupId, LocalDate date, Integer lessonNumber);

    @Modifying
    void deleteByTeacherId(Long teacherId);

    @Modifying
    void deleteByGroupId(Long groupId);

    @Modifying
    void deleteBySubjectId(Long subjectId);

    @EntityGraph(attributePaths = {"teacher", "subject", "group"})
    Optional<Lesson> findWithDetailsById(Long id);


}