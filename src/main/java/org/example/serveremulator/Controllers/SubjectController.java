package org.example.serveremulator.Controllers;


import org.example.serveremulator.DTO.SubjectRequest;
import org.example.serveremulator.DTO.SubjectResponse;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Mappers.SubjectMapper;
import org.example.serveremulator.Services.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<SubjectResponse> getAllSubjects() {
        return subjectService.getAllSubjects().stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id); // Бросит NotFoundException если не найдено
        return ResponseEntity.ok(subjectMapper.toResponse(subject));
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
        Subject subject = subjectMapper.toEntity(request);
        Subject createdSubject = subjectService.createSubject(subject);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subjectMapper.toResponse(createdSubject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody SubjectRequest request) {
        Subject subject = subjectMapper.toEntity(request);
        Subject updatedSubject = subjectService.updateSubject(id, subject);
        return ResponseEntity.ok(subjectMapper.toResponse(updatedSubject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}