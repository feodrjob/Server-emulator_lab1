package org.example.serveremulator.Controllers;

import org.example.serveremulator.DTO.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getAllLessons() {
        List<LessonResponse> lessons = lessonService.getAllLessons().stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<LessonResponse>> response = new ApiResponse<>(true,lessons);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);

        ApiResponse<LessonResponse> response = new ApiResponse<>(true, lessonResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessonsByTeacher(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<LessonResponse> lesson = lessonService.getLessonsByTeacherId(teacherId,start,end).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<LessonResponse>> response = new ApiResponse<> (true,lesson);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessonsByGroup(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<LessonResponse> lesson = lessonService.getLessonsByGroupId(groupId, start,end).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<LessonResponse>> response = new ApiResponse<> (true,lesson);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@RequestBody LessonRequest request) {
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

        ApiResponse<LessonResponse> apiResponse = new ApiResponse<>(true, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(@PathVariable Long id, @RequestBody LessonRequest request) {
        Lesson lesson = lessonMapper.toEntity(request);
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);
        Lesson lessonWithDetails = lessonService.getLessonById(updatedLesson.getId());
        LessonResponse lessonResponse = lessonMapper.toResponse(lessonWithDetails);

        ApiResponse<LessonResponse> response = new ApiResponse<>(true, lessonResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        ApiResponse<Void> response = new ApiResponse<>(true,null);
        return ResponseEntity.ok(response);
    }
}