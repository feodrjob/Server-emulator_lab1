package org.example.serveremulator.Repositories;

import org.example.serveremulator.Entityes.Lesson;

import org.example.serveremulator.Entityes.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByGroupIdAndDateAndLessonNumber(Long groupId, LocalDate date, Integer lessonNumber);

    //Для проверки конфликтов при созддании
    @EntityGraph(attributePaths = {"teacher","subject", "group"})
    Optional<Lesson> findWithDetailsById(Long id);

    //Запрос с пагинацией и фильтрами
    @EntityGraph(attributePaths = {"teacher", "subject", "group"})
    @Query("SELECT l FROM Lesson l WHERE l.date BETWEEN :startDate AND :endDate " +
            "AND (:groupId IS NULL OR l.group.id = :groupId) " +
            "AND (:teacherId IS NULL OR l.teacher.id = :teacherId)")
    Page<Lesson> findLessonsWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("groupId") Long groupId,
            @Param("teacherId") Long teacherId,
            Pageable pageable
    );
}