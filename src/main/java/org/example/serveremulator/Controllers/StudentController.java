package org.example.serveremulator.Controllers;

import jakarta.validation.Valid;
import org.example.serveremulator.DTO.ApiResponse;
import org.example.serveremulator.DTO.StudentRequest;
import org.example.serveremulator.DTO.StudentResponse;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Mappers.StudentMapper;
import org.example.serveremulator.Services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    // просмотр списка всех студентов доступен всем
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents().stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<StudentResponse>> response = new ApiResponse<>(true, students);
        return ResponseEntity.ok(response);
    }

    // просмотр карточки конкретного студента доступен всем
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        StudentResponse studentResponse = studentMapper.toResponse(student);

        ApiResponse<StudentResponse> response = new ApiResponse<>(true, studentResponse);
        return ResponseEntity.ok(response);
    }

    // просмотр студентов определенной группы доступен всем
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByGroup(
            @PathVariable Long groupId) {
        List<StudentResponse> student = studentService.getStudentsByGroupId(groupId).stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());

        ApiResponse<List<StudentResponse>> response = new ApiResponse<>(true, student);
        return ResponseEntity.ok(response);
    }

    // создание профиля студента доступно только админу
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        Student createdStudent = studentService.createStudent(student);
        StudentResponse students = studentMapper.toResponse(createdStudent);

        ApiResponse<StudentResponse> response = new ApiResponse<>(true, students);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // редактирование данных студента доступно только админу
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        Student updatedStudent = studentService.updateStudent(id, student);
        StudentResponse students = studentMapper.toResponse(updatedStudent);

        ApiResponse<StudentResponse> response = new ApiResponse<>(true, students);
        return ResponseEntity.ok(response);
    }

    // удаление студента доступно только админу
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);

        ApiResponse<Void> response = new ApiResponse<>(true, null);
        return ResponseEntity.ok(response);
    }
}