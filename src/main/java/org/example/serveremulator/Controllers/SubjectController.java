package org.example.serveremulator.Controllers;

import jakarta.validation.Valid;
import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.SubjectRequest;
import org.example.serveremulator.DTO.SubjectResponse;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Mappers.SubjectMapper;
import org.example.serveremulator.Services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    // просмотр всех дисциплин доступен всем
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        List<SubjectResponse> subjects = subjectService.getAllSubjects().stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<SubjectResponse>> response = new ApiResponse<>(true, subjects);
        return ResponseEntity.ok(response);
    }

    // просмотр конкретной дисциплины доступен всем
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
        // метод сервиса сам выбросит ошибку, если сущность не найдена
        Subject subject = subjectService.getSubjectById(id);
        SubjectResponse subjectResponse = subjectMapper.toResponse(subject);

        ApiResponse<SubjectResponse> response = new ApiResponse<>(true, subjectResponse);
        return ResponseEntity.ok(response);
    }

    // создание дисциплины доступно только админу
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@Valid @RequestBody SubjectRequest request) {
        Subject subject = subjectMapper.toEntity(request);
        Subject createdSubject = subjectService.createSubject(subject);
        SubjectResponse subjectResponse = subjectMapper.toResponse(createdSubject);

        ApiResponse<SubjectResponse> response = new ApiResponse<>(true, subjectResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // редактирование дисциплины доступно только админу
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        Subject subject = subjectMapper.toEntity(request);
        Subject updatedSubject = subjectService.updateSubject(id, subject);
        SubjectResponse subjectResponse = subjectMapper.toResponse(updatedSubject);

        ApiResponse<SubjectResponse> response = new ApiResponse<>(true, subjectResponse);
        return ResponseEntity.ok(response);
    }

    // удаление дисциплины доступно только админу
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);

        // данных для возврата нет
        ApiResponse<Void> response = new ApiResponse<>(true, null);
        return ResponseEntity.ok(response);
    }
}