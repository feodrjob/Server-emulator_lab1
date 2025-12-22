package org.example.serveremulator.Services;
import jakarta.transaction.Transactional;
import org.example.serveremulator.Entityes.Teacher;
import org.example.serveremulator.Repositories.StudentRepository;
import org.example.serveremulator.Repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.example.serveremulator.Enums.ErrorCode;
import org.example.serveremulator.Exceptions.NotFoundException;
import org.example.serveremulator.Exceptions.ValidationException;

@Service
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher ID must be positive number"
            );
        }

        return teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.TEACHER_NOT_FOUND,
                        "Teacher with id " + id + " not found"
                ));
    }
    public Teacher createTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher cannot be null"
            );
        }

        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "First name is required"
            );
        }
        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty()) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Last name is required"
            );
        }

        // Исправляем опечатку: getMidleName() -> getMiddleName()
        String middleName = teacher.getMidleName(); // или teacher.getMiddleName() если исправили в сущности

        if (teacherRepository.existsByLastNameAndFirstNameAndMiddleName(
                teacher.getLastName(),
                teacher.getFirstName(),
                middleName)) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher with this name already exists"
            );
        }

        // Тримим строки
        teacher.setFirstName(teacher.getFirstName().trim());
        teacher.setLastName(teacher.getLastName().trim());
        if (teacher.getMidleName() != null) {
            teacher.setMidleName(teacher.getMidleName().trim());
        }

        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Long id, Teacher teacher) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher ID must be positive number"
            );
        }

        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.TEACHER_NOT_FOUND,
                        "Teacher with id " + id + " not found"
                ));

        // Исправляем логику условий (было ИЛИ, должно быть И)
        if (teacher.getFirstName() != null && !teacher.getFirstName().trim().isEmpty()) {
            existingTeacher.setFirstName(teacher.getFirstName().trim());
        }

        if (teacher.getLastName() != null && !teacher.getLastName().trim().isEmpty()) {
            existingTeacher.setLastName(teacher.getLastName().trim());
        }

        if (teacher.getMidleName() != null) {
            existingTeacher.setMidleName(teacher.getMidleName().trim());
        }

        return teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException(
                    ErrorCode.VALIDATION_ERROR,
                    "Teacher ID must be positive number"
            );
        }

        if (!teacherRepository.existsById(id)) {
            throw new NotFoundException(
                    ErrorCode.TEACHER_NOT_FOUND,
                    "Teacher with id " + id + " not found"
            );
        }

        teacherRepository.deleteById(id);
    }
}