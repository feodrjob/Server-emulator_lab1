package org.example.serveremulator.Controllers;

import org.example.serveremulator.DTO.StudentRequest;
import org.example.serveremulator.DTO.StudentResponse;
import org.example.serveremulator.Entityes.Student;
import org.example.serveremulator.Mappers.StudentMapper;
import org.example.serveremulator.Services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(studentMapper.toResponse(student));
    }
    @GetMapping("/group/{groupId}")
    public List<StudentResponse> getStudentsByGroup(@PathVariable Long groupId) {
        return studentService.getStudentsByGroupId(groupId).stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        try {
            Student student = studentMapper.toEntity(request);
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(studentMapper.toResponse(createdStudent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
        try {
            Student student = studentMapper.toEntity(request);
            Student updatedStudent = studentService.updateStudent(id, student);
            return ResponseEntity.ok(studentMapper.toResponse(updatedStudent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}