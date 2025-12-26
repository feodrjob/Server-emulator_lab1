package org.example.serveremulator.Services;

import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Lesson;
import org.example.serveremulator.Entityes.Subject;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;
import org.example.serveremulator.Repositories.AttendanceRepository;
import org.example.serveremulator.Repositories.LessonRepository;
import org.example.serveremulator.Repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          LessonRepository lessonRepository,
                          AttendanceRepository attendanceRepository) {
        this.subjectRepository = subjectRepository;
        this.lessonRepository = lessonRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject ID must be positive number"
            );
        }

        return subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.SUBJECT_NOT_FOUND,
                        "Subject with id " + id + " not found"
                ));
    }

    public Subject createSubject(Subject subject) {
        if (subject == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject cannot be null"
            );
        }

        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject name is required"
            );
        }

        String subjectName = subject.getName().trim();

        if (subjectRepository.existsByName(subjectName)) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject with name '" + subjectName + "' already exists"
            );
        }

        subject.setName(subjectName);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject subject) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject ID must be positive number"
            );
        }

        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.SUBJECT_NOT_FOUND,
                        "Subject with id " + id + " not found"
                ));

        if (subject.getName() != null && !subject.getName().trim().isEmpty()
                && !subject.getName().equals(existingSubject.getName())) {

            String newName = subject.getName().trim();

            if (subjectRepository.existsByName(newName)) {
                throw new ValidationException(
                        ErrorCode.VALIDATION_ERROR,
                        "Subject with name '" + newName + "' already exists"
                );
            }
            existingSubject.setName(newName);
        }
        return subjectRepository.save(existingSubject);
    }

    public void deleteSubject(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Subject ID must be positive number"
            );
        }

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.SUBJECT_NOT_FOUND,
                        "Subject with id " + id + " not found"
                ));

        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(lesson -> lesson.getSubject() != null &&
                        lesson.getSubject().getId().equals(id))
                .collect(Collectors.toList());

        for (Lesson lesson : lessons) {
            attendanceRepository.deleteByLessonId(lesson.getId());
        }

        lessonRepository.deleteBySubjectId(id);

        subjectRepository.delete(subject);
    }
}