package org.example.serveremulator.Controllers;

import org.example.serveremulator.DTO.LessonRequest;
import org.example.serveremulator.DTO.LessonResponse;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Mappers.LessonMapper;
import org.example.serveremulator.Services.LessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    public LessonController(LessonService lessonService, LessonMapper lessonMapper) {
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public List<LessonResponse> getAllLessons() {
        return lessonService.getAllLessons().stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lessonMapper.toResponse(lesson));
    }

    @GetMapping("/teacher/{teacherId}")
    public List<LessonResponse> getLessonsByTeacher(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return lessonService.getLessonsByTeacherId(teacherId, start, end).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/group/{groupId}")
    public List<LessonResponse> getLessonsByGroup(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return lessonService.getLessonsByGroupId(groupId, start, end).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@RequestBody LessonRequest request) {
        logger.info("POST /api/lessons - Создание занятия. Request: {}", request);

        Lesson lesson = lessonMapper.toEntity(request);
        logger.info("Создана сущность Lesson: id={}, date={}, lessonNumber={}",
                lesson.getId(), lesson.getDate(), lesson.getLessonNumber());

        Lesson createdLesson = lessonService.createLesson(lesson);
        logger.info("Сохранена в БД Lesson с id: {}", createdLesson.getId());

        // Перезагружаем с деталями
        Lesson lessonWithDetails = lessonService.getLessonById(createdLesson.getId());
        logger.info("Перезагружена Lesson с деталями. Teacher: {}, Subject: {}, Group: {}",
                lessonWithDetails.getTeacher(),
                lessonWithDetails.getSubject(),
                lessonWithDetails.getGroup());

        LessonResponse response = lessonMapper.toResponse(lessonWithDetails);
        logger.info("Создан Response: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long id, @RequestBody LessonRequest request) {
        Lesson lesson = lessonMapper.toEntity(request);
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);

        // ПЕРЕЗАГРУЖАЕМ с связанными сущностями
        Lesson lessonWithDetails = lessonService.getLessonById(updatedLesson.getId());

        return ResponseEntity.ok(lessonMapper.toResponse(lessonWithDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}