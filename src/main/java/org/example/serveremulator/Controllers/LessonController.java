package org.example.serveremulator.Controllers;

import jakarta.validation.Valid;
import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.LessonRequest;
import org.example.serveremulator.DTO.LessonResponse;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Mappers.LessonMapper;
import org.example.serveremulator.Services.LessonService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<Page<LessonResponse>>> getLessons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long groupId,     // Необязательный фильтр
            @RequestParam(required = false) Long teacherId,   // Необязательный фильтр
            @RequestParam(defaultValue = "0") int page,       // Пагинация: номер страницы (с 0)
            @RequestParam(defaultValue = "10") int size       // Пагинация: размер страницы
    ) {
        //получаем фильтрованные данные
        Page<Lesson> lessonPage = lessonService.getLessonFiltred(startDate, endDate, groupId, teacherId, page, size);
        //оборачиваем в ответ
        Page<LessonResponse> responsePage = lessonPage.map(lessonMapper::toResponse);
        ApiResponse<Page<LessonResponse>> response = new ApiResponse<>(true, responsePage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);

        ApiResponse<LessonResponse> response = new ApiResponse<>(true, lessonResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@Valid @RequestBody LessonRequest request) {
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
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(@PathVariable Long id,@Valid @RequestBody LessonRequest request) {
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