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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

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

    // просматривать расписание могут все
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<LessonResponse>>> getLessons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long groupId,     // необязательный фильтр
            @RequestParam(required = false) Long teacherId,   // необязательный фильтр
            @RequestParam(defaultValue = "0") int page,       // пагинация: номер страницы
            @RequestParam(defaultValue = "10") int size       // пагинация: размер страницы
    ) {
        // получение отфильтрованных данных
        Page<Lesson> lessonPage = lessonService.getLessonFiltred(startDate, endDate, groupId, teacherId, page, size);

        // упаковка ответа
        Page<LessonResponse> responsePage = lessonPage.map(lessonMapper::toResponse);
        ApiResponse<Page<LessonResponse>> response = new ApiResponse<>(true, responsePage);
        return ResponseEntity.ok(response);
    }

    // смотреть конкретное занятие могут все
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);

        ApiResponse<LessonResponse> response = new ApiResponse<>(true, lessonResponse);
        return ResponseEntity.ok(response);
    }

    // создавать занятия могут преподаватели и админы
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@Valid @RequestBody LessonRequest request) {

        Lesson lesson = lessonMapper.toEntity(request);


        Lesson createdLesson = lessonService.createLesson(lesson);

        // перезагрузка данных с деталями из базы
        Lesson lessonWithDetails = lessonService.getLessonById(createdLesson.getId());


        LessonResponse response = lessonMapper.toResponse(lessonWithDetails);

        ApiResponse<LessonResponse> apiResponse = new ApiResponse<>(true, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // редактировать занятия могут преподаватели и админы
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonRequest request) {
        Lesson lesson = lessonMapper.toEntity(request);
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);

        // перезагрузка данных
        Lesson lessonWithDetails = lessonService.getLessonById(updatedLesson.getId());
        LessonResponse lessonResponse = lessonMapper.toResponse(lessonWithDetails);

        ApiResponse<LessonResponse> response = new ApiResponse<>(true, lessonResponse);
        return ResponseEntity.ok(response);
    }

    // удалять занятия может только админ
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);

        // данных для возврата нет, отдаем null
        ApiResponse<Void> response = new ApiResponse<>(true, null);
        return ResponseEntity.ok(response);
    }
}