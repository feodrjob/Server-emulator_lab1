package org.example.serveremulator.Controllers;

import jakarta.validation.Valid;
import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.TeacherRequest;
import org.example.serveremulator.DTO.TeacherResponse;
import org.example.serveremulator.Entityes.Teacher;
import org.example.serveremulator.Mappers.TeacherMapper;
import org.example.serveremulator.Services.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    // просмотр всех преподавателей доступен всем
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<TeacherResponse>>> getAllTeachers(
            // параметры из url
            @RequestParam(defaultValue = "0") int page,
            // значения по умолчанию для пагинации
            @RequestParam(defaultValue = "10") int size
    ) {
        // получение страницы из базы
        Page<Teacher> teacherPage = teacherService.findAll(page, size);
        Page<TeacherResponse> responsePage = teacherPage.map(teacherMapper::toResponse);
        ApiResponse<Page<TeacherResponse>> apiResponse = new ApiResponse<>(true, responsePage);
        return ResponseEntity.ok(apiResponse);
    }

    // просмотр конкретного преподавателя доступен всем
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> getTeacher(@PathVariable Long id) {
        Teacher teacher = teacherService.findById(id);
        TeacherResponse teacherResponse = teacherMapper.toResponse(teacher);

        ApiResponse<TeacherResponse> apiResponse = new ApiResponse<>(true, teacherResponse);
        return ResponseEntity.ok(apiResponse);
    }

    // добавление профиля преподавателя доступно только админу
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(@Valid @RequestBody TeacherRequest request) {
        Teacher teacher = teacherMapper.toEntity(request);
        Teacher createdTeacher = teacherService.createTeacher(teacher);
        TeacherResponse teacherResponse = teacherMapper.toResponse(createdTeacher);

        ApiResponse<TeacherResponse> apiResponse = new ApiResponse<>(true, teacherResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // редактирование профиля преподавателя доступно только админу
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        Teacher teacher = teacherMapper.toEntity(request);
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        TeacherResponse teacherResponse = teacherMapper.toResponse(updatedTeacher);

        ApiResponse<TeacherResponse> apiResponse = new ApiResponse<>(true, teacherResponse);
        return ResponseEntity.ok(apiResponse);
    }

    // удаление преподавателя доступно только админу
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);

        ApiResponse<Void> apiResponse = new ApiResponse<>(true, null);
        return ResponseEntity.ok(apiResponse);
    }
}