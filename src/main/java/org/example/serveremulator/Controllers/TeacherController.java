package org.example.serveremulator.Controllers;

import org.example.serveremulator.DTO.TeacherRequest;
import org.example.serveremulator.DTO.TeacherResponse;
import org.example.serveremulator.Entityes.Teacher;
import org.example.serveremulator.Mappers.TeacherMapper;
import org.example.serveremulator.Services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public List<TeacherResponse> getAllTeachers() {
        return teacherService.findAll().stream()
                .map(teacherMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping ("/{id}")
    public ResponseEntity<TeacherResponse> getTeacher(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.findById(id).
                    orElseThrow(() -> new IllegalArgumentException("id is null or id <= 0"));
            return ResponseEntity.ok(teacherMapper.toResponse(teacher));
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody TeacherRequest request) {
        try{
            Teacher teacher = teacherMapper.toEntity(request);
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(teacherMapper.toResponse(createdTeacher));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping ("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable Long id, @RequestBody TeacherRequest request) {
        try{
            Teacher teacher = teacherMapper.toEntity(request);
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            return ResponseEntity.ok(teacherMapper.toResponse(updatedTeacher));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void>  deleteTeacher(@PathVariable Long id) {
        try{
            teacherService.deleteTeacher(id);
            return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
}
