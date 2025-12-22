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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
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
        Lesson lesson = lessonService.getLessonById(id); // Будет брошено исключение если не найдено
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
        Lesson lesson = lessonMapper.toEntity(request);
        Lesson createdLesson = lessonService.createLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonMapper.toResponse(createdLesson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long id, @RequestBody LessonRequest request) {
        Lesson lesson = lessonMapper.toEntity(request);
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);
        return ResponseEntity.ok(lessonMapper.toResponse(updatedLesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}